package ltd.fdsa.maven.codegg;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.base.Strings;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.var;
import org.apache.maven.model.Developer;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@EqualsAndHashCode(callSuper = true)
@Mojo(name = "egg", defaultPhase = LifecyclePhase.CLEAN, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Data
public class EggMojo extends AbstractMojo {

    Log log = this.getLog();
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    @Parameter(property = "packageName", defaultValue = "")
    private String packageName;
    @Parameter(property = "parentName", defaultValue = "")
    private String parentName;
    @Parameter(property = "author", required = true, defaultValue = "zhumingwu@126.com")
    private String author;

    @Override
    @SneakyThrows
    public void execute() {
        log.info("Try to generate code in " + String.join(".", this.parentName, this.packageName));
        log.info("------------------------------------------------------------------------");
        log.info(this.project.getBasedir().getAbsolutePath());
        if (Strings.isNullOrEmpty(this.parentName)) {
            this.parentName = this.project.getGroupId();
        }
        if (Strings.isNullOrEmpty(this.packageName)) {
            this.packageName = String.join(".", this.project.getArtifactId().split("-"));
        }
        List<Developer> developers = this.project.getDevelopers();
        if (developers.stream().count() > 0) {
            this.author = developers.get(0).getName();
        }
        log.info("------------------------------------------------------------------------");

        try {
            log.info("Generate file start");

            //得到模板文件
            var root = EggMojo.class.getClassLoader().getResources("./templates");
            if (root == null) {
                log.info("failed!");
                return;
            }

            while (root.hasMoreElements()){
                System.out.println(root.nextElement());
            }
//            for (var file : find(new File(root.toURI()))) {
//                log.info(file.getAbsolutePath());
//            }
            //得到class描述

            JarFile jarFile = new JarFile("D:\\Work\\java\\fast-data\\maven-plugin\\demo\\target\\demo-2.1.5-SNAPSHOT.jar");
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()){
                 log.info(entries.nextElement().toString());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                if (childPackage) {
                    myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
                }
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath      jar文件路径
     * @param childPackage 是否遍历子包
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
        List<String> myClassName = new ArrayList<String>();
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (childPackage) {
                        if (entryName.startsWith(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    } else {
                        int index = entryName.lastIndexOf("/");
                        String myPackagePath;
                        if (index != -1) {
                            myPackagePath = entryName.substring(0, index);
                        } else {
                            myPackagePath = entryName;
                        }
                        if (myPackagePath.equals(packagePath)) {
                            entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                            myClassName.add(entryName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    private List<File> find(File dirFile) {

        List<File> result = new ArrayList<File>();

        if (!dirFile.exists()) {
            return result;
        }
        if (dirFile.isFile()) {
            result.add(dirFile);
            return result;
        }
        for (File file : dirFile.listFiles()) {
            result.addAll(find(file));
        }
        return result;
    }

    @SneakyThrows
    private void copyFile(String fileName, File targetFile) {
        InputStream initialStream = EggMojo.class.getClassLoader().getResourceAsStream(fileName);
        assert initialStream != null;
        log.info(targetFile.getCanonicalPath());
        Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        initialStream.close();
    }
}