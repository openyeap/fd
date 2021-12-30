### 简介

Mybatis通用mapper，支持简单的表关系注释配置，无侵入，无需多余配置，只需要继承基础接口即可

### 快速使用

+ 目前没有发布到中心仓库，可以把项目克隆下来，用maven编译为jar包使用maven本地引入。

```xml
<dependency>
    <groupId>%groupId%</groupId>
    <artifactId>%artifactId%</artifactId>
    <version>%version%</version>
    <scope>system</scope>
    <systemPath>${project.basedir}\src\main\libs\%jarName%.jar</systemPath>
</dependency>
```

+ 创建实体

```java

@Table("demo")
public class Demo {

    @Primary
    private String id;

    private String name;

    private String birthday;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

```

+ 创建mapper接口继承BaseMapper

```java
@Repository
public interface DemoMapper extends BaseMapper<Demo, String> {
}
```

+ 创建测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonMapperApplicationTests {

    @Autowired
    DemoMapper demoMapper;

    @Test
    public void contextLoads() {
        demoMapper.select();
    }

}
```

### 注解

#### **@Table** 配置实体对应表

+ value：数据库表名

#### **@Column** 配置实体属性和对应字段信息

+ value：对应数据库字段，如果不配默认为属性名称，驼峰命名会转为下划线命名
+ order：是否排序
+ orderType：排序方式，默认desc

#### **@Primary** 配置属性为主键

+ value：属性是主键

#### **@JoinColumn** 配置关联表

+ tableName：要关联的表
+ column：要展示关联表中的哪个字段
+ relations：字段关系

#### **@Association** 字段关联关系

+ target：当前实体对应表中字段
+ association：关联表中对应字段

#### **@Transient** 配置属性不在数据库中

**如需分页建议用Pagehelper**