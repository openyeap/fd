
import annotation.*;
import demo.IEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import function.StringLengthMethod;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import model.Entity;
import model.Field;
import model.Module;
import model.RelationDefine;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Slf4j
public class Egg {



    public void execute() {
        log.info("------------------------------------------------------------------------");
        log.info("Try to generate code in ");
        log.info("------------------------------------------------------------------------");

        try {
            log.info("Generate file start");

            var builder = Module.builder();
            builder.name("project name");
            builder.description("description of the project");
            log.info("得到class描述");
            ClassLoader classLoader = new ClassLoader(System.getProperty("user.dir"));
            builder.entities(getEntities(classLoader, classLoader.loadClasses(entry -> entry.getName().endsWith(".class"), clazz -> IEntity.class.isAssignableFrom(clazz) && !IEntity.class.equals(clazz))));
            builder.relations(getRelations(classLoader, classLoader.loadClasses(entry -> entry.getName().endsWith(".class"), clazz -> IEntity.class.isAssignableFrom(clazz) && !IEntity.class.equals(clazz))));
            log.info(builder.build().toString());
            log.info("得到模板文件 ");


            // step1 创建freeMarker配置实例
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);


            var root = new File(Egg.class.getClassLoader().getResource("./templates").toURI());
            log.info(root.getAbsoluteFile().getPath());
            // step2 获取模版路径
            configuration.setDirectoryForTemplateLoading(root);

            // step3 创建数据模型
            var data = new HashMap<String, Object>();
            data.put("module", builder.build());
            data.put("ll", new StringLengthMethod());
            Writer out = null;
            try {
                // step4 加载模版文件
                for (var file : find(root)) {
                    if (file.getName().endsWith(".ftl")) {
                        for (var entity : builder.build().getEntities()) {
                            var source = file.getPath().substring(0, file.getPath().lastIndexOf(".ftl"));
                            Template fileNameTemplate = new Template("file", source, configuration);
                            StringWriter result = new StringWriter();
                            data.put("entity", entity);
                            fileNameTemplate.process(data, result);
                            var targetName = result.toString().substring(root.getPath().length() + 1);
                            var templateName = file.getPath().substring(root.getPath().length() + 1).replace("\\", "/");
                            Template template = configuration.getTemplate(templateName);
                            // step5 生成数据
                            File targetFile = new File("./output/" + targetName);
                            if (!targetFile.getParentFile().exists()) {
                                targetFile.getParentFile().mkdirs();
                            }
                            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));
                            // step6 输出文件
                            template.process(data, out);
                        }
                    } else {
                        var templateName = file.getPath().substring(root.getPath().length() + 1).replace("\\", "/");
                        Template template = configuration.getTemplate(templateName);
                        // step5 生成数据
                        File targetFile = new File("./output/" + templateName);
                        if (!targetFile.getParentFile().exists()) {
                            targetFile.getParentFile().mkdirs();
                        }
                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile)));
                        // step6 输出文件
                        template.process(data, out);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (null != out) {
                        out.flush();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }


        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private RelationDefine[] getRelations(ClassLoader classLoader, List<Class<?>> classes) {
        List<RelationDefine> results = new ArrayList<RelationDefine>();
        for (var clazz : classes) {
            for (var item : classLoader.getDeclaredFields(clazz)) {
                var builder = RelationDefine.builder();
                var relation = item.getAnnotation(Relation.class);
                if (relation == null) {
                    continue;
                }
                builder.name(relation.value());
                builder.fromEntity(relation.entity());
                builder.fromField(relation.field());
                builder.toEntity(clazz);
                builder.toField(item.getName());
                results.add(builder.build());
            }
        }
        return results.toArray(new RelationDefine[0]);
    }

    private Entity[] getEntities(ClassLoader classLoader, List<Class<?>> classes) {

        List<Entity> results = new ArrayList<Entity>();
        for (var item : classes) {
            var builder = Entity.builder();
            builder.code(item.getCanonicalName());
            var name = item.getAnnotation(Name.class);
            if (name == null) {
                builder.name(item.getSimpleName());
            } else {
                builder.name(name.value());
            }
            var remark = item.getAnnotation(Remark.class);
            if (remark == null) {
                builder.remark(item.getSimpleName());
            } else {
                builder.remark(remark.value());
            }
            builder.fields(getFields(classLoader, item));
            results.add(builder.build());
        }
        return results.toArray(new Entity[0]);
    }

    private Field[] getFields(ClassLoader classLoader, Class<?> clazz) {
        List<Field> results = new ArrayList<Field>();
        for (var item : classLoader.getDeclaredFields(clazz)) {
            var builder = Field.builder();
            builder.code(clazz.getCanonicalName() + "." + item.getName());
            var name = item.getAnnotation(Name.class);
            if (name == null) {
                builder.name(item.getName());
            } else {
                builder.name(name.value());
            }
            var type = item.getAnnotation(Type.class);
            if (type == null) {
                builder.type(item.getType().getSimpleName());
            } else {
                builder.type(type.value());
            }
            var remark = item.getAnnotation(Remark.class);
            if (remark == null) {
                builder.remark(item.getName());
            } else {
                builder.remark(remark.value());
            }
            var isNull = item.getAnnotation(IsNull.class);
            if (isNull == null) {
                builder.isNull(false);
            } else {
                builder.isNull(isNull.value());
            }
            var length = item.getAnnotation(Length.class);
            if (length == null) {
                builder.length(8);
            } else {
                builder.length(length.value());
            }
            var scale = item.getAnnotation(Scale.class);
            if (scale == null) {
                builder.scale(3);
            } else {
                builder.scale(scale.value());
            }
            var autoIncrement = item.getAnnotation(AutoIncrement.class);
            if (autoIncrement == null) {
                builder.autoIncrement(false);
            } else {
                builder.autoIncrement(autoIncrement.value());
            }
            results.add(builder.build());
        }
        return results.toArray(new Field[0]);
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
}