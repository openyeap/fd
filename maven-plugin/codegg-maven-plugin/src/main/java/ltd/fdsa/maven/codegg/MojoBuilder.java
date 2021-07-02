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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Mojo(name = "init", defaultPhase = LifecyclePhase.CLEAN, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
@Data
public class MojoBuilder extends AbstractMojo {

    private static String files = "application-dev.yml\n" +
            "application-prd.yml\n" +
            "application-uat.yml\n" +
            "application.yml\n" +
            "assembly.xml\n" +
            "banner.txt\n" +
            "Dockerfile\n" +
            "install\n" +
            "install.bat\n" +
            "mybatis-config.xml\n" +
            "start.bat\n" +
            "start.sh";
    Log log = this.getLog();
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;
    @Parameter(property = "override", required = true, defaultValue = "false")
    private Boolean override;
    @Parameter(property = "dbDriver", required = true, defaultValue = "org.postgresql.Driver")
    private String dbDriver;

    @Parameter(property = "dbUrl", required = true, defaultValue = "jdbc:mysql://127.0.0.1:3306/dev_database?useSSL=false&characterEncoding=utf-8")
    private String dbUrl;
    @Parameter(property = "dbUserName", required = true, defaultValue = "root")
    private String dbUserName;
    @Parameter(property = "dbPassword", required = true, defaultValue = "MySQL123$")
    private String dbPassword;
    @Parameter(property = "dbSchema", required = true, defaultValue = "public")
    private String dbSchema;
    @Parameter(property = "dbType", required = true, defaultValue = "MYSQL")
    private DbType dbType;
    @Parameter(property = "prefixes", required = true, defaultValue = "s_,t_,r_")
    private String[] prefixes;
    @Parameter(property = "tables", required = true, defaultValue = "s_user,s_role")
    private String[] tables;
    @Parameter(property = "packageName", defaultValue = "")
    private String packageName;
    @Parameter(property = "parentName", defaultValue = "")
    private String parentName;
    @Parameter(property = "author", required = true, defaultValue = "zhumingwu@126.com")
    private String author;

    @Override
    @SneakyThrows
    public void execute() {
        if (Strings.isNullOrEmpty(this.parentName)) {
            this.parentName = this.project.getGroupId();
        }
        if (Strings.isNullOrEmpty(this.packageName)) {
            this.packageName = String.join(".", this.project.getArtifactId().split("-"));
        }
        this.log.info("Try to generate code in " + String.join(".", this.parentName, this.packageName));
        log.info("------------------------------------------------------------------------");
        List<Developer> developers = this.project.getDevelopers();
        if (developers.stream().count() > 0) {
            this.author = developers.get(0).getName();
        }
        if (this.override) {
            this.log.warn("override: " + this.override.toString());
        } else {
            this.log.info("    override: " + this.override.toString());
        }
        this.log.info("      dbType: " + this.dbType);
        this.log.info("    dbDriver: " + this.dbDriver);
        this.log.info("       dbUrl: " + this.dbUrl);
        this.log.info("    dbSchema: " + this.dbSchema);
        this.log.info("  dbUserName: " + this.dbUserName);
        this.log.info("  dbPassword: " + this.dbPassword);
        this.log.info("      tables: " + String.join("; ", this.tables));
        this.log.info("    prefixes: " + String.join("; ", this.prefixes));

        log.info("------------------------------------------------------------------------");

        try {
            log.info("Generate file start");
            generate(false);
            generate(true);


            for (String fileName : files.split("\n")) {
                try {
                    copyFile(fileName, new File(System.getProperty("user.dir") + "/src/main/resources/" + fileName));
                } catch (Exception ex) {
                    log.error("copyFile", ex);
                }
            }
            String projectPath = System.getProperty("user.dir") + "/src/main/java";
            for (File file : find(projectPath)) {
                String path = file.getCanonicalPath();
                if (path.contains("readmappers") && path.endsWith("Mapper.java")) {
                    path = path.replace("Mapper.java", "Reader.java");
                    File dest = new File(path);
                    if (dest.exists()) {
                        dest.delete();
                    }
                    log.info(dest.getCanonicalPath());
                    file.renameTo(dest);
                    continue;
                }
                if (path.contains("writemappers") && path.endsWith("Mapper.java")) {
                    path = path.replace("Mapper.java", "Writer.java");
                    File dest = new File(path);
                    if (dest.exists()) {
                        dest.delete();
                    }
                    log.info(dest.getCanonicalPath());
                    file.renameTo(dest);
                }
            }
            log.info("Generate file end");

        } catch (Exception e) {
            log.error("execute", e);
        }
    }

    private DataSourceConfig getDataSourceConfig() {
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(this.dbUrl);
        dsc.setSchemaName(this.dbSchema);
        dsc.setDriverName(this.dbDriver);
        dsc.setUsername(this.dbUserName);
        dsc.setPassword(this.dbPassword);
        dsc.setDbType(this.dbType);
        return dsc;
    }

    @SneakyThrows
    private List<File> find(String pathName) {

        List<File> result = new ArrayList<File>();
        File dirFile = new File(pathName);
        if (!dirFile.exists()) {
            return result;
        }
        if (dirFile.isFile()) {
            result.add(dirFile);
            return result;
        }
        for (File file : dirFile.listFiles()) {
            result.addAll(find(file.getCanonicalPath()));
        }
        return result;
    }

    @SneakyThrows
    private void copyFile(String fileName, File targetFile) {
        if (targetFile.exists() && !this.override) {
            return;
        }
        InputStream initialStream = MojoBuilder.class.getClassLoader().getResourceAsStream(fileName);
        assert initialStream != null;
        this.log.info(targetFile.getCanonicalPath());
        Files.copy(initialStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        initialStream.close();
    }

    @SneakyThrows
    private void generate(boolean reader) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(this.author);
        gc.setOpen(false);
        gc.setFileOverride(this.override);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        mpg.setDataSource(this.getDataSourceConfig());

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(this.packageName);
        pc.setParent(this.parentName);
        if (reader) {
            pc.setMapper("database.readmappers");
        } else {
            pc.setMapper("database.writemappers");

        }
        pc.setEntity("model.entity");

        mpg.setPackageInfo(pc);
        String packageName = this.parentName + "." + this.packageName;
        String name;
        if (reader) {
            name = "Reader";
        } else {
            name = "Writer";
        }

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("packageName", packageName);
                this.setMap(map);
            }
        };

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        String templatePath = "/templates/mapper.xml.vm";
        // 自定义配置会被优先输出
        focList.add(
                new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名
                        return projectPath
                                + "/src/main/resources/mybatisMappers/"
                                + pc.getModuleName()
                                + "/"
                                + tableInfo.getEntityName()
                                + name + ".xml";
                    }
                });

        templatePath = "/templates/testService.java.vm";
        focList.add(
                new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名
                        return projectPath
                                + "/src/test/java/"
                                + pc.getParent().replace(".", "/")
                                + "/Test"
                                + tableInfo.getEntityName()
                                + "Service.java";
                    }
                });

        templatePath = "/templates/application.java.vm";
        focList.add(
                new FileOutConfig(templatePath) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        // 自定义输出文件名
                        return projectPath
                                + "/src/main/java/"
                                + pc.getParent().replace(".", "/")
                                + "/Application.java";
                    }
                });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 配置自定义输出模板
        //templateConfig.setEntity("templates/entity2.java");
        //templateConfig.setServiceImpl(null);
        //templateConfig.setController("templates/controller2.java");
        //templateConfig.setMapper("templates/mapper2.java");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass("ltd.fdsa.database.controller.BaseController");
        if (reader) {
            strategy.setSuperMapperClass("ltd.fdsa.database.ReadMapper");
        } else {
            strategy.setSuperMapperClass("ltd.fdsa.database.WriteMapper");
        }
        strategy.setSuperServiceClass("ltd.fdsa.database.service.IService");
        strategy.setSuperServiceImplClass("ltd.fdsa.database.service.BaseService");
        strategy.setInclude(this.tables);
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        //	strategy.setTablePrefix(pc.getModuleName() + "_");
        strategy.setTablePrefix(this.prefixes);
        strategy.setEntityTableFieldAnnotationEnable(true);

        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine());
        mpg.execute();
    }


}