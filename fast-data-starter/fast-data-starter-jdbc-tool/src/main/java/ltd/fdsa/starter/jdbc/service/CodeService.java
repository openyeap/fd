package ltd.fdsa.starter.jdbc.service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.starter.jdbc.config.JdbcProperty;

@Slf4j
@Service
public class CodeService {

	@Autowired
	protected JdbcProperty property;

	@SneakyThrows
	public Object generate() {

		String projectPath = System.getProperty("user.dir") + "/src/main/java";

		String pn = this.property.getPackageName();
		String[] tbs = this.property.getTable();
		log.info("======================");
		log.info(this.property.toString());
		log.info("======================");

		generateReader(pn, tbs, getDataSourceConfig());
		generateWriter(pn, tbs, getDataSourceConfig());
//		for (File file : find(projectPath)) {
//			String path = file.getCanonicalPath();
//			if (path.contains("readMappers")) {
//				System.out.println(path);
//				path = path.substring(0, path.length() - 11).concat("Reader.java");
//				System.out.println(path);
//				File dest = new File(path);
//				file.renameTo(dest);
//			}
//			if (path.contains("writeMappers")) {
//				System.out.println(path);
//				path = path.substring(0, path.length() - 11).concat("Writer.java");
//				System.out.println(path);
//				File dest = new File(path);
//				file.renameTo(dest);
//			}
//		}

		return "OK";
	}

	DataSourceConfig getDataSourceConfig() {// 数据源配置

		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(this.property.getURL());
		dsc.setSchemaName(this.property.getSchemaName());
		dsc.setDriverName(this.property.getDriver());
		dsc.setUsername(this.property.getUserName());
		dsc.setPassword(this.property.getPassword());
		dsc.setDbType(this.property.getDbType());
		return dsc;
	}

	@SneakyThrows
	List<File> find(String pathName) {

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
	void generateReader(String pn, String[] tbs, DataSourceConfig dsc) {

		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath + "/src/main/java");

		log.info("========={}=========", projectPath + "/src/main/java");
		gc.setAuthor("system");
		gc.setOpen(false);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(pn);
		pc.setParent(this.property.getParantName());
		pc.setMapper("database.readMappers");
		pc.setEntity("model.entity");

		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "Reader");
				this.setMap(map);
			}
		};

		// 如果模板引擎是 freemarker
		// String templatePath = "/templates/mapper.xml.ftl";
		// 如果模板引擎是 velocity
		String templatePath = "/templates/mapper.xml.vm";

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath) {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				return projectPath + "/src/main/resources/mybatisMappers/" + pc.getModuleName() + "/"
						+ tableInfo.getEntityName() + "Reader.xml";
			}
		});

		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);

		// 配置模板
		TemplateConfig templateConfig = new TemplateConfig();

		// 配置自定义输出模板
		// 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
		templateConfig.setEntity("templates/entity2.java");
		// templateConfig.setService();
		templateConfig.setController("templates/controller2.java");
		templateConfig.setMapper("templates/mapper2.java");
		templateConfig.setXml(null);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		// strategy.setSuperEntityClass("com.baomidou.mybatisplus.activerecord.Model");
		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);
		strategy.setSuperControllerClass("ltd.fdsa.starter.jdbc.controller.BaseController");
		strategy.setSuperMapperClass("ltd.fdsa.starter.jdbc.ReadMapper");
		strategy.setSuperServiceClass("ltd.fdsa.starter.jdbc.service.IService");
		strategy.setSuperServiceImplClass("ltd.fdsa.starter.jdbc.service.BaseService");
		strategy.setInclude(tbs);
		strategy.setSuperEntityColumns("id");
		strategy.setControllerMappingHyphenStyle(true);
//	strategy.setTablePrefix(pc.getModuleName() + "_");
		strategy.setTablePrefix(this.property.getTablePrefix());
		strategy.setEntityTableFieldAnnotationEnable(true);

		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine());
		// 切换为 freemarker 模板引擎
		// mpg.setTemplateEngine(new
		// com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine());
		// 切换为 beetl 模板引擎
		// mpg.setTemplateEngine(new
		// com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine());
		mpg.execute();
	}

	void generateWriter(String pn, String[] tbs, DataSourceConfig dsc) {

		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setAuthor("system");
		gc.setOpen(false);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(pn);
		pc.setParent(this.property.getParantName());
		pc.setMapper("database.writeMappers");
		pc.setEntity("model.entity");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "Writer");
				this.setMap(map);
			}
		};

		// 如果模板引擎是 freemarker
		// String templatePath = "/templates/mapper.xml.ftl";
		// 如果模板引擎是 velocity
		String templatePath = "/templates/mapper.xml.vm";

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		// 自定义配置会被优先输出
		focList.add(new FileOutConfig(templatePath) {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输出文件名
				return projectPath + "/src/main/resources/mybatisMappers/" + pc.getModuleName() + "/"
						+ tableInfo.getEntityName() + "Writer.xml";
			}
		});

		cfg.setFileOutConfigList(focList);

		mpg.setCfg(cfg);
		// 配置模板
		TemplateConfig templateConfig = new TemplateConfig();

		// 配置自定义输出模板
		// 指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
		templateConfig.setEntity("templates/entity2.java");
		// templateConfig.setService();
		templateConfig.setController("templates/controller2.java");
		templateConfig.setMapper("templates/mapper2.java");
		templateConfig.setXml(null);
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		// strategy.setSuperEntityClass("com.baomidou.mybatisplus.activerecord.Model");
		strategy.setEntityLombokModel(true);
		strategy.setRestControllerStyle(true);
		strategy.setSuperControllerClass("ltd.fdsa.starter.jdbc.controller.BaseController");
		strategy.setSuperMapperClass("ltd.fdsa.starter.jdbc.WriteMapper");
		strategy.setSuperServiceClass("ltd.fdsa.starter.jdbc.service.IService");
		strategy.setSuperServiceImplClass("ltd.fdsa.starter.jdbc.service.BaseService");
		strategy.setInclude(tbs);
		strategy.setSuperEntityColumns("id");
		strategy.setControllerMappingHyphenStyle(true);
		// strategy.setTablePrefix(pc.getModuleName() + "_");
		strategy.setTablePrefix(this.property.getTablePrefix());
		strategy.setEntityTableFieldAnnotationEnable(true);

		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine());
		// 切换为 freemarker 模板引擎
		// mpg.setTemplateEngine(new
		// com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine());
		// 切换为 beetl 模板引擎
		// mpg.setTemplateEngine(new
		// com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine());
		mpg.execute();
	}

	String[] getTableList() {
		try (Scanner scanner = new Scanner(System.in);) {
			StringBuilder help = new StringBuilder();
			help.append("请输入表名：");
			List<String> list = new ArrayList<String>(1000);
			while (true) {
				System.out.println(help.toString());
				String ipt = scanner.next();
				if ("exit".equals(ipt)) {
					String[] toBeStored = list.toArray(new String[list.size()]);
					return toBeStored;
				}
				list.addAll(Arrays.asList(ipt.split(",")));
			}
		}
	}
}

class myFileFilter implements FileFilter {

	public myFileFilter() {

	}

	public boolean accept(File pathname) {

		if (pathname.getName().endsWith("Mapper.java"))
			return true;
		return false;
	}
}
