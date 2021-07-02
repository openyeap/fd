### 简介

Jpa统用数据库访问层，项目上只需要定义Entity和Service即可实现基础的增删改查。

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

+ 创建UserService继承BaseService

```java

```

+ 创建测试类

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonMapperApplicationTests {

    @Autowired
    UserService service;

    @Test
    public void contextLoads() {
        service.select();
    }

}
```

