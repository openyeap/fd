#   limiter-spring-boot-starter

限流   SpringBoot 版

[English](./README.md)

支持功能：
- [x] 单机限流
- [x] 集群限流

## 提示：
    每次重启或新增一个节点,初始数量会向redis填充,当然,这是在容量未达到最大情况下  
    具体流量限制参数,请配合压力测试工具来调优,达到您所需要的QPS  
    限流成功,则执行含有@LimitTraffic注解代码块,否则不执行,便于用户自定义方案,如服务保护降级等操作  
    为提高性能,不采取强一致性,限流误差最大值为：集群数量-1  
        
##  Quick Start

```xml
<!--在pom.xml中添加依赖-->
        <dependency>
            <artifactId>limiter-spring-boot-starter</artifactId>
            <groupId>com.github.thierrysquirrel</groupId>
            <version>1.0.3-RELEASE</version>
        </dependency>
```

### 配置文件
 
 ```properties
 ## application.properties
spring.redis.host= #您redis的地址
spring.redis.port= #您redis的端口号
 ```
 
#   启动limiter

 ```java
 @SpringBootApplication
 @EnableLimiter
 public class DemoApplication{
     public static void main(String[] args){
         SpringApplication.run(DemoApplication.class, args);
     }  
 }
 ```
 
 #  限流
 
 ```java
 @TokenLimitedTraffic
 public class Hello {
 	@LimitTraffic(initialQuantity = 2333, maximumCapacity = 3222, addedQuantity = 2333)
 	public String hello() {
 		return "world";
 	}
 }
 ```
 
 #  自定义操作

```java
@RestController
public class World {
	@Resource
	private Hello hello;

	@GetMapping("/world")
	public String world() {
		String hello = this.hello.hello();
		boolean empty = StringUtils.isEmpty(hello);
		if (empty) {
			return "服务降级";
		}
		return hello;
	}
}
``` 