# Spring Event

## 配置ApplicationListener 监听器的6种方式

1. 配置文件中通过context.listener.classes配置
2. 在resources目录下新建META-INF文件夹并新建spring.factories文件通过org.springframework.context.ApplicationListener配置
3. 在启动main函数中通过SpringApplication配置SpringApplication.addListeners(你的监听器);
4. 通过实现接口org.springframework.context.ApplicationContextInitializer,得到context后通过编程式,设置监听器
5. 实现ApplicationListener，并使用@Configuration 注解配置,同时可以配合@Order(-100)设置优先级
6. 使用@EventListener 注解配置在bean中定义任意方法并使用该注解, 注解属性class中可以指定具体监控的事件类,通过方法参数指定事件类型,如果不指定则表示监控所有的事件

> 我们主要推荐使用后面两种实现方式

## 分布式事件

可以通过Redis消息订阅实现跨进程通信，计划完成：

1. 服务发现事件。

2. 配置变更事件。

3. 远程通知事件。
