//package cn.zhumingwu.starter.jdbc.mappers;
//
//
//import lombok.experimental.var;
//
//import java.lang.reflect.Field;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.stream.Collectors;
//
//public class TableInfoHelper {
//    public static class TableInfo {
//
//    }
//
//    private static final Map<Class<?>, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap();
//    private static final String DEFAULT_ID_NAME = "id";
//
//    public TableInfoHelper() {
//    }
//
//    public static TableInfo getTableInfo(Class<?> clazz) {
//
//        TableInfo tableInfo = (TableInfo) TABLE_INFO_CACHE.get(clazz);
//
//        if (null != tableInfo) {
//            return tableInfo;
//        }
//
//        for (Class currentClass = clazz;
//             null == tableInfo && Object.class != currentClass;
//             tableInfo = (TableInfo) TABLE_INFO_CACHE.get(currentClass)) {
//            currentClass = currentClass.getSuperclass();
//        }
//
//        if (tableInfo != null) {
//            TABLE_INFO_CACHE.put(clazz, tableInfo);
//        }
//
//        return tableInfo;
//
//    }
//
//
//    public static synchronized TableInfo initTableInfo(Class<?> clazz) {
//        TableInfo targetTableInfo = (TableInfo) TABLE_INFO_CACHE.get(clazz);
//
//        if (targetTableInfo != null) {
//            Configuration oldConfiguration = targetTableInfo.getConfiguration();
//            if (!oldConfiguration.equals(configuration)) {
//                targetTableInfo = initTableInfo(configuration, builderAssistant.getCurrentNamespace(), clazz);
//                TABLE_INFO_CACHE.put(clazz, targetTableInfo);
//            }
//
//            return targetTableInfo;
//        } else {
//            return (TableInfo) TABLE_INFO_CACHE.computeIfAbsent(clazz, (key) -> {
//                return initTableInfo(key);
//            });
//        }
//    }
//
//    private static synchronized TableInfo initTableInfo(Class<?> clazz) {
//        var tableName = clazz.getName();
//        var table = clazz.getAnnotation(jakarta.annotation.sql.DataSourceDefinition.class);
//        if (table != null)
//            tableName = table.name();
//        for (var f : clazz.getFields()) {
//            var fieldName = f.getName();
//            var  field = f.getAnnotation(jakarta.annotation.sql.DataSourceDefinition.class);
//            if (field != null) {
//                fieldName = field.name();
//            }
//        }
//        TableInfo tableInfo = new TableInfo(clazz);
//        tableInfo.setCurrentNamespace(currentNamespace);
//        tableInfo.setConfiguration(configuration);
//        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
//        String[] excludeProperty = initTableName(clazz, globalConfig, tableInfo);
//        List<String> excludePropertyList = excludeProperty != null && excludeProperty.length > 0 ?
//                Arrays.asList(excludeProperty) : Collections.emptyList();
//        initTableFields(clazz, globalConfig, tableInfo, excludePropertyList);
//        tableInfo.initResultMapIfNeed();
//        LambdaUtils.installCache(tableInfo);
//        return tableInfo;
//    }
//
//    private static String[] initTableName(Class<?> clazz, GlobalConfig globalConfig, TableInfo tableInfo) {
//        DbConfig dbConfig = globalConfig.getDbConfig();
//        TableName table = (TableName) clazz.getAnnotation(TableName.class);
//        String tableName = clazz.getSimpleName();
//        String tablePrefix = dbConfig.getTablePrefix();
//        String schema = dbConfig.getSchema();
//        boolean tablePrefixEffect = true;
//        String[] excludeProperty = null;
//        if (table != null) {
//            if (StringUtils.isNotBlank(table.value())) {
//                tableName = table.value();
//                if (StringUtils.isNotBlank(tablePrefix) && !table.keepGlobalPrefix()) {
//                    tablePrefixEffect = false;
//                }
//            } else {
//                tableName = initTableNameWithDbConfig(tableName, dbConfig);
//            }
//
//            if (StringUtils.isNotBlank(table.schema())) {
//                schema = table.schema();
//            }
//
//            if (StringUtils.isNotBlank(table.resultMap())) {
//                tableInfo.setResultMap(table.resultMap());
//            }
//
//            tableInfo.setAutoInitResultMap(table.autoResultMap());
//            excludeProperty = table.excludeProperty();
//        } else {
//            tableName = initTableNameWithDbConfig(tableName, dbConfig);
//        }
//
//        String targetTableName = tableName;
//        if (StringUtils.isNotBlank(tablePrefix) && tablePrefixEffect) {
//            targetTableName = tablePrefix + tableName;
//        }
//
//        if (StringUtils.isNotBlank(schema)) {
//            targetTableName = schema + "." + targetTableName;
//        }
//
//        tableInfo.setTableName(targetTableName);
//        if (null != dbConfig.getKeyGenerator()) {
//            tableInfo.setKeySequence((KeySequence) clazz.getAnnotation(KeySequence.class));
//        }
//
//        return excludeProperty;
//    }
//
//    private static String initTableNameWithDbConfig(String className, DbConfig dbConfig) {
//        String tableName = className;
//        if (dbConfig.isTableUnderline()) {
//            tableName = StringUtils.camelToUnderline(className);
//        }
//
//        if (dbConfig.isCapitalMode()) {
//            tableName = tableName.toUpperCase();
//        } else {
//            tableName = StringUtils.firstToLowerCase(tableName);
//        }
//
//        return tableName;
//    }
//
//    private static void initTableFields(Class<?> clazz, GlobalConfig globalConfig, TableInfo tableInfo, List<String> excludeProperty) {
//        DbConfig dbConfig = globalConfig.getDbConfig();
//        ReflectorFactory reflectorFactory = tableInfo.getConfiguration().getReflectorFactory();
//        Reflector reflector = reflectorFactory.findForClass(clazz);
//        List<Field> list = getAllFields(clazz);
//        boolean isReadPK = false;
//        boolean existTableId = isExistTableId(list);
//        boolean existTableLogic = isExistTableLogic(list);
//        List<TableFieldInfo> fieldList = new ArrayList(list.size());
//        Iterator var12 = list.iterator();
//
//        while (true) {
//            while (true) {
//                Field field;
//                do {
//                    if (!var12.hasNext()) {
//                        tableInfo.setFieldList(fieldList);
//                        if (!isReadPK) {
//                            logger.warn(String.format("Can not find table primary key in Class: \"%s\".", clazz.getName()));
//                        }
//
//                        return;
//                    }
//
//                    field = (Field) var12.next();
//                } while (excludeProperty.contains(field.getName()));
//
//                if (existTableId) {
//                    TableId tableId = (TableId) field.getAnnotation(TableId.class);
//                    if (tableId != null) {
//                        if (isReadPK) {
//                            throw ExceptionUtils.mpe("@TableId can't more than one in Class: \"%s\".", new Object[]{clazz.getName()});
//                        }
//
//                        initTableIdWithAnnotation(dbConfig, tableInfo, field, tableId, reflector);
//                        isReadPK = true;
//                        continue;
//                    }
//                } else if (!isReadPK) {
//                    isReadPK = initTableIdWithoutAnnotation(dbConfig, tableInfo, field, reflector);
//                    if (isReadPK) {
//                        continue;
//                    }
//                }
//
//                TableField tableField = (TableField) field.getAnnotation(TableField.class);
//                if (tableField != null) {
//                    fieldList.add(new TableFieldInfo(dbConfig, tableInfo, field, tableField, reflector, existTableLogic));
//                } else {
//                    fieldList.add(new TableFieldInfo(dbConfig, tableInfo, field, reflector, existTableLogic));
//                }
//            }
//        }
//    }
//
//    public static boolean isExistTableId(List<Field> list) {
//        return list.stream().anyMatch((field) -> {
//            return field.isAnnotationPresent(TableId.class);
//        });
//    }
//
//    public static boolean isExistTableLogic(List<Field> list) {
//        return list.stream().anyMatch((field) -> {
//            return field.isAnnotationPresent(TableLogic.class);
//        });
//    }
//
//    private static void initTableIdWithAnnotation(DbConfig dbConfig, TableInfo tableInfo, Field field, TableId tableId, Reflector reflector) {
//        boolean underCamel = tableInfo.isUnderCamel();
//        String property = field.getName();
//        if (field.getAnnotation(TableField.class) != null) {
//            logger.warn(String.format("This \"%s\" is the table primary key by @TableId annotation in Class: \"%s\",So @TableField annotation will not work!", property, tableInfo.getEntityType().getName()));
//        }
//
//        if (IdType.NONE == tableId.type()) {
//            tableInfo.setIdType(dbConfig.getIdType());
//        } else {
//            tableInfo.setIdType(tableId.type());
//        }
//
//        String column = property;
//        if (StringUtils.isNotBlank(tableId.value())) {
//            column = tableId.value();
//        } else {
//            if (underCamel) {
//                column = StringUtils.camelToUnderline(property);
//            }
//
//            if (dbConfig.isCapitalMode()) {
//                column = column.toUpperCase();
//            }
//        }
//
//        Class<?> keyType = reflector.getGetterType(property);
//        if (keyType.isPrimitive()) {
//            logger.warn(String.format("This primary key of \"%s\" is primitive !不建议如此请使用包装类 in Class: \"%s\"", property, tableInfo.getEntityType().getName()));
//        }
//
//        tableInfo.setKeyRelated(checkRelated(underCamel, property, column)).setKeyColumn(column).setKeyProperty(property).setKeyType(keyType);
//    }
//
//    private static boolean initTableIdWithoutAnnotation(DbConfig dbConfig, TableInfo tableInfo, Field field, Reflector reflector) {
//        String property = field.getName();
//        if ("id".equalsIgnoreCase(property)) {
//            if (field.getAnnotation(TableField.class) != null) {
//                logger.warn(String.format("This \"%s\" is the table primary key by default name for `id` in Class: \"%s\",So @TableField will not work!", property, tableInfo.getEntityType().getName()));
//            }
//
//            String column = property;
//            if (dbConfig.isCapitalMode()) {
//                column = property.toUpperCase();
//            }
//
//            Class<?> keyType = reflector.getGetterType(property);
//            if (keyType.isPrimitive()) {
//                logger.warn(String.format("This primary key of \"%s\" is primitive !不建议如此请使用包装类 in Class: \"%s\"", property, tableInfo.getEntityType().getName()));
//            }
//
//            tableInfo.setKeyRelated(checkRelated(tableInfo.isUnderCamel(), property, column)).setIdType(dbConfig.getIdType()).setKeyColumn(column).setKeyProperty(property).setKeyType(keyType);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static boolean checkRelated(boolean underCamel, String property, String column) {
//        column = StringUtils.getTargetColumn(column);
//        String propertyUpper = property.toUpperCase(Locale.ENGLISH);
//        String columnUpper = column.toUpperCase(Locale.ENGLISH);
//        if (!underCamel) {
//            return !propertyUpper.equals(columnUpper);
//        } else {
//            return !propertyUpper.equals(columnUpper) && !propertyUpper.equals(columnUpper.replace("_", ""));
//        }
//    }
//
//    public static List<Field> getAllFields(Class<?> clazz) {
//        List<Field> fieldList = ReflectionKit.getFieldList(ClassUtils.getUserClass(clazz));
//        return (List) fieldList.stream().filter((field) -> {
//            TableField tableField = (TableField) field.getAnnotation(TableField.class);
//            return tableField == null || tableField.exist();
//        }).collect(Collectors.toList());
//    }
//
//    public static KeyGenerator genKeyGenerator(String baseStatementId, TableInfo tableInfo, MapperBuilderAssistant builderAssistant) {
//        IKeyGenerator keyGenerator = GlobalConfigUtils.getKeyGenerator(builderAssistant.getConfiguration());
//        if (null == keyGenerator) {
//            throw new IllegalArgumentException("not configure IKeyGenerator implementation class.");
//        } else {
//            Configuration configuration = builderAssistant.getConfiguration();
//            String id = builderAssistant.getCurrentNamespace() + "." + baseStatementId + "!selectKey";
//            ResultMap resultMap = (new Builder(builderAssistant.getConfiguration(), id, tableInfo.getKeyType(), new ArrayList())).build();
//            MappedStatement mappedStatement = (new org.apache.ibatis.mapping.MappedStatement.Builder(builderAssistant.getConfiguration(), id, new StaticSqlSource(configuration, keyGenerator.executeSql(tableInfo.getKeySequence().value())), SqlCommandType.SELECT)).keyProperty(tableInfo.getKeyProperty()).resultMaps(Collections.singletonList(resultMap)).build();
//            configuration.addMappedStatement(mappedStatement);
//            return new SelectKeyGenerator(mappedStatement, true);
//        }
//    }
//}
