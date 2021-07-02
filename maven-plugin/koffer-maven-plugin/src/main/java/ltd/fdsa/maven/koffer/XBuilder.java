package ltd.fdsa.maven.koffer;

import io.loadkit.Loaders;
import io.loadkit.Resource;
import lombok.Data;
import lombok.SneakyThrows;
import ltd.fdsa.koffer.XKit;
import ltd.fdsa.koffer.boot.XBoot;
import ltd.fdsa.koffer.filter.XAllEntryFilter;
import ltd.fdsa.koffer.filter.XAnyEntryFilter;
import ltd.fdsa.koffer.filter.XMixEntryFilter;
import ltd.fdsa.koffer.jar.Koffer;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Map;

@Mojo(name = "build", defaultPhase = LifecyclePhase.PACKAGE, requiresProject = true, inheritByDefault = true, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Data
public class XBuilder extends AbstractMojo {
    /**
     * 当前Maven工程
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * 加密密钥长度
     */
    @Parameter(property = "keySize", required = true, defaultValue = "128")
    private int keySize;

    /**
     * 加密向量长度
     */
    @Parameter(property = "ivSize", required = true, defaultValue = "128")
    private int ivSize;

    /**
     * 加密密码
     */
//    @Parameter(property = "password", required = true, defaultValue = "abc123$")
//    private String password;

    /**
     * 原本JAR所在文件夹
     */
    @Parameter(property = "sourceDir", required = true, defaultValue = "${project.build.directory}")
    private File sourceDir;

    /**
     * 原本JAR名称
     */
    @Parameter(
            property = "sourceJar",
            required = true,
            defaultValue = "${project.build.finalName}.jar")
    private String sourceJar;

    /**
     * 生成JAR所在文件夹
     */
    @Parameter(property = "targetDir", required = true, defaultValue = "${project.build.directory}")
    private File targetDir;

    /**
     * 生成JAR名称
     */
    @Parameter(
            property = "targetJar",
            required = true,
            defaultValue = "${project.build.finalName}.kjar")
    private String targetJar;

    /**
     * 包含资源。 使用Ant表达式，例如： io/Koffer/** com/company/project/** mapper/*Mapper.xml
     */
    @Parameter(property = "includes", required = true, defaultValue = "com/**,*.xml,*.yml,*.yaml")
    private String[] includes;

    /**
     * 排除资源。 使用Ant表达式，例如： io/Koffer/** static/** META-INF/resources/**
     */
    @Parameter(property = "excludes")
    private String[] excludes;

    /**
     * 加密完成后需要删除的资源 支持Ant表达式，例如： target/*.jar ../module/target/*.jar
     */
    @Parameter(property = "deletes")
    private String[] deletes;

    @SneakyThrows
    private String CheckSum(String input) {
        Log log = this.getLog();
        MessageDigest hash = MessageDigest.getInstance("SHA-1");
        byte[] buf = input.getBytes();
        hash.update(buf, 0, buf.length);
        String output = bytesToHex(hash.digest());
        log.info(output);
        return output;
    }

    private String bytesToHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString().toLowerCase();
    }

    @Override
    @SneakyThrows
    public void execute() {
        Log log = this.getLog();
        String packaging = project.getPackaging();
        if (!"jar".equalsIgnoreCase(packaging)) {
            log.info("Skip for packaging: " + packaging);
            return;
        }

        try {
            File src = new File(sourceDir, sourceJar);
            File dest = new File(targetDir, targetJar);
            File folder = dest.getParentFile();
            if (!folder.exists() && !folder.mkdirs() && !folder.exists()) {
                throw new IOException("could not make directory: " + folder);
            }

            String password = CheckSum(dest.getName());
            log.info("=============================");
            /**
             * 加密算法名称
             */
            String algorithm = "AES/CBC/PKCS5Padding";
            log.info("Using algorithm: " + algorithm);
            log.info("Using keySize: " + this.keySize);
            log.info("Using ivSize: " + this.ivSize);
            log.info("Using fileName: " + dest.getName());
            log.info("Using password: " + password);

            XMixEntryFilter<JarArchiveEntry> filter;
            if (XArray.isEmpty(includes) && XArray.isEmpty(excludes)) {
                filter = null;
                log.info("Including all resources");
            } else {
                filter = XKit.all();
                if (!XArray.isEmpty(includes)) {
                    XAnyEntryFilter<JarArchiveEntry> including = XKit.any();
                    for (String include : includes) {
                        including.mix(new XIncludeAntEntryFilter(include));
                        log.info("Including " + include);
                    }
                    filter.mix(including);
                }
                if (!XArray.isEmpty(excludes)) {
                    XAllEntryFilter<JarArchiveEntry> excluding = XKit.all();
                    for (String exclude : excludes) {
                        excluding.mix(new XExcludeAntEntryFilter(exclude));
                        log.info("Excluding " + exclude);
                    }
                    filter.mix(excluding);
                }
            }

            Build build = project.getBuild();
            Map<String, Plugin> plugins = build.getPluginsAsMap();
            Plugin plugin = plugins.get("org.springframework.boot:spring-boot-maven-plugin");
            // 非Spring-Boot项目/模块
            if (plugin == null) {
                Koffer.encrypt(src, dest, XKit.key(algorithm, keySize, ivSize, password), filter);
            }
            // Spring-Boot项目/模块
            else {

                Object configuration = plugin.getConfiguration();
                // 不允许开启 <executable>true<executable>
                if (configuration instanceof Xpp3Dom) {
                    Xpp3Dom dom = (Xpp3Dom) configuration;
                    {
                        Xpp3Dom child = dom.getChild("executable");
                        String executable = child != null ? child.getValue() : null;
                        if ("true".equalsIgnoreCase(executable)) {
                            String msg = "Unsupported to build an Koffer for an <executable>true</executable> spring boot JAR file, ";
                            msg += "maybe you should upgrade Koffer-maven-plugin dependency if it have been supported in the later versions,";
                            msg += "if not, delete <executable>true</executable> or set executable as false for the configuration of spring-boot-maven-plugin.";
                            throw new MojoFailureException(msg);
                        }
                    }
                    {
                        Xpp3Dom child = dom.getChild("embeddedLaunchScript");
                        String embeddedLaunchScript = child != null ? child.getValue() : null;
                        if (embeddedLaunchScript != null) {
                            String msg = "Unsupported to build an Koffer for an <embeddedLaunchScript>...</embeddedLaunchScript> spring boot JAR file, ";
                            msg += "maybe you should upgrade Koffer-maven-plugin dependency if it have been supported in the later versions,";
                            msg += "if not, delete <embeddedLaunchScript>...</embeddedLaunchScript> for the configuration of spring-boot-maven-plugin.";
                            throw new MojoFailureException(msg);
                        }
                    }
                }
                log.info("=============================");
                log.info("Koffer File: " + dest.getAbsolutePath());
                XBoot.encrypt(src, dest, XKit.key(algorithm, keySize, ivSize, password), filter);
            }

            File root = project.getFile().getParentFile();
            for (int i = 0; deletes != null && i < deletes.length; i++) {
                String delete = deletes[i];
                File dir = root;
                while (delete.startsWith("../")) {
                    dir = dir.getParentFile() != null ? dir.getParentFile() : dir;
                    delete = delete.substring("../".length());
                }
                log.info("Deleting file(s) matching pattern: " + dir.getCanonicalPath().replace('\\', '/') + "/" + delete);
                Enumeration<Resource> resources = Loaders.ant(Loaders.file(dir)).load(delete);
                while (resources.hasMoreElements()) {
                    Resource resource = resources.nextElement();
                    URL url = resource.getUrl();
                    String path = url.getPath();
                    File file = new File(path);
                    log.debug("Deleting file: " + file.getCanonicalPath());
                    if (!file.delete()) {
                        log.warn("Could not delete file: " + file);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new MojoExecutionException("could not build koffer", e);
        }
    }
}