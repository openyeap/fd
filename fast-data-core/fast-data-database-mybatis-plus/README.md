# 简介

使用Mybatis Plus构建数据库操作。
## 快速使用

### 创建实体

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_role")
public class Role extends BaseEntity<Integer> {

    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "value" )
    private  Integer value;

}
```
> 注：实体需要使用 Mybatis Plus的注解，比如：表使用@TableName，字段使用@TableField，主键使用@TableId等
> 
### 创建mapper接口继承BaseMapper

```java
@MybatisPlusMapper
public interface RoleReader extends ReadMapper<Role> {

}
```

```java
@MybatisPlusMapper
public interface RoleWriter extends WriteMapper<Role> {

}
```
> 注: Mapper需要使用注解@MybatisPlusMapper

### 创建service实现扩展MybatisPlusService

```java
@Service
public class RoleServiceImpl extends MybatisPlusService<Role, Integer, RoleWriter, RoleReader> implements RoleService {

}
```

> 注：建议项目上定义业务逻辑接口，比如：RoleService，使用MybatisPlusService使用默认业务（数据库操作）扩展。