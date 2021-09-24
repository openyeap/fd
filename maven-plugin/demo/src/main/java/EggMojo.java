
import annotation.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import model.Entity;
import model.Field;
import model.Module;
import model.RelationDefine;


import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Slf4j
public class EggMojo {

    public java.lang.reflect.Field[] getDeclaredFields(Class<?> clazz) {
        ArrayList<java.lang.reflect.Field> fieldList = new ArrayList<>(16);
        while (clazz != null) {
            var fields = clazz.getDeclaredFields();
            var sss = Stream.of(fields).collect(Collectors.toList());
            fieldList.addAll(sss);
            clazz = clazz.getSuperclass();
        }
        java.lang.reflect.Field[] f = new java.lang.reflect.Field[fieldList.size()];
        return fieldList.toArray(f);
    }

    private RelationDefine[] getRelations(List<Class<?>> classes) {
        List<RelationDefine> results = new ArrayList<RelationDefine>();
        for (var clazz : classes) {
            for (var item :  getDeclaredFields(clazz)) {
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

    private Entity[] getEntities(List<Class<?>> classes) {

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
            builder.fields(getFields(item));
            results.add(builder.build());
        }
        return results.toArray(new Entity[0]);
    }

    private Field[] getFields(Class<?> clazz) {
        List<Field> results = new ArrayList<Field>();
        for (var item : getDeclaredFields(clazz)) {
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
            builder.entities(getEntities(classLoader.loadClasses(entry -> entry.getName().endsWith(".class"), clazz -> IEntity.class.isAssignableFrom(clazz)
                    && !IEntity.class.equals(clazz)
            )));
            builder.relations(getRelations(classLoader.loadClasses(entry -> entry.getName().endsWith(".class"), clazz -> IEntity.class.isAssignableFrom(clazz))));
            log.info(builder.build().toString());
            log.info("得到模板文件 ");


            // step1 创建freeMarker配置实例
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);


            var root = new File(EggMojo.class.getClassLoader().getResource("./templates").toURI());
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
                            File targetFile = new File("./" + targetName);
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
                        File targetFile = new File("./" + templateName);
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
}