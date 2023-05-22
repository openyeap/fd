# 基础设施结构

## 网络层
keep live + nginx/traefik

## 接入层
spring cloud gateway + eureka

## 服务层
spring boot applications

## 监控层
prometheus + grafana

## 数据层
PGSQL 一主多从服务， influxdb轻量数据记录，elk重量日志服务



#  项目结构

## 代码
一个项目中除主程序源代码（src/main/java），还需要包含：
- 单元测试（src/test/java）：核心的领域模型，包括领域对象(比如Order类)，Factory类，领域服务类等；
- 数据库脚本（sql）：包括数据库表结构设计创建或脚本和原始数据的创建脚本
- 资源或配置（src/main/resouces)


## 文档
README.md + doc
readme包括但不限于：
- 项目简介：用一两句话简单描述该项目所实现的业务功能；
- 技术选型：列出项目的技术栈，包括语言、框架和中间件等；
- 本地构建：列出本地开发过程中所用到的工具命令；
- 领域模型：核心的领域概念，比如对于示例电商系统来说有Order、Product等；
- 测试策略：自动化测试如何分类，哪些必须写测试，哪些没有必要写测试；
- 技术架构：技术架构图；
- 部署架构：部署架构图；
- 外部依赖：项目运行时所依赖的外部集成方，比如订单系统会依赖于会员系统；
- 环境信息：各个环境的访问方式，数据库连接等；
- 编码实践：统一的编码实践，比如异常处理原则、分页封装等；
- FAQ：开发过程中常见问题的解答。
readme中尽量做到一指禅，详细内容可以在doc目录分类讨论描述。
注意：文档一定要持续更新，否则，会造成文档和代码的不一致。

## 脚本
在一个项目中除了源代码，文档还需要辅助的脚本，包括但不限于
- 运行脚本 
- 安装或部署脚本
- 自动化测试脚本
## 工具


# 代码结构
## fast-data-component
常用工具类封装，代码辅助生成工具或开发相关生产工具
## cloud
gateway - 网关，集成统一认证
register - 服务注册中心
middle office - 管理和配置系统的界面，集成系统日志和监控等UI
## service
各种业务的restful api
back office - 统一用户、权限，系统等管理

## ui
front end - 前端项目

# 功能结构


## 运维
日志处理
- 链路追踪，比如MDC(Mapped Diagnostic Context)
- 多节点下，日志集中统一

异常处理
- 格式统一
- 上下文和结构化信息
- 异常的唯一标识
性能与健康检查
- 初步检查程序是否运行正常
- 负载均衡软件会通过一个健康检查URL判断节点的可达性



## 中间件

分布式锁，可以使用的技术包括Shedlock、Redis、ZooKeeper和Hazelcast等的分布式锁实现机制
数据库 mysql pgsql sqlite influxdb redis 


## 工具
- Checkstyle：用于检查代码格式，规范编码风格
- Spotbugs：Findbugs的继承者
- Dependency check：OWASP提供的Java类库安全性检查
- Sonar：用于代码持续改进的跟踪
- swagger：接口文档
- flyway：数据库迁移
- Guava：来自Google的常用类库
- Apache Commons：来自Apache的常用类库
- Mockito：主要用于单元测试的mock
- DBUnit：测试中管理数据库测试数据
- Rest Assured：用于Rest API测试
- Jackson 2：Json数据的序列化和反序列化
- jjwt：Jwt token认证(暂时未使用过)
- Lombok：自动生成常见Java代码，比如equals()方法，getter和setter等；
- Feign：声明式Rest客户端
- Tika：用于准确检测文件类型
- itext：生成Pdf文件等
- zxing：生成二维码
- Xstream：比Jaxb更轻量级的XML处理库

