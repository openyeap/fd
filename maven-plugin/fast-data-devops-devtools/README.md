# devtools

devtools自动代码生成工具，基于SpringBoot2.0 + Spring Data Jpa + Thymeleaf + Shiro 开发的后台管理系统，采用分模块的方式便于开发和维护，支持前后台模块分别部署，目前支持的功能有：权限管理、部门管理、字典管理、日志记录、文件上传、代码生成等，为快速开发后台系统而生的脚手架！

## 技术选型

- 后端技术：SpringBoot + Spring Data Jpa + Thymeleaf + Shiro + Jwt + EhCache

- 前端技术：Layui + Jquery  + zTree + Font-awesome

## 全新的项目结构

![项目结构图](https://oscimg.oschina.net/oscnet/4e8e47b3801ba98767dc25a1a6efbb522fe.jpg)

## 功能列表

- 用户管理：用于管理后台系统的用户，可进行增删改查等操作。
- 角色管理：分配权限的最小单元，通过角色给用户分配权限。
- 菜单管理：用于配置系统菜单，同时也作为权限资源。
- 部门管理：通过不同的部门来管理和区分用户。
- 字典管理：对一些需要转换的数据进行统一管理，如：男、女等。
- 行为日志：用于记录用户对系统的操作，同时监视系统运行时发生的错误。
- 文件上传：内置了文件上传接口，方便开发者使用文件上传功能。
- 代码生成：可以帮助开发者快速开发项目，减少不必要的重复操作，花更多精力注重业务实现。
- 表单构建：通过拖拽的方式快速构建一个表单模块。
- 数据接口：根据业务代码自动生成相关的api接口文档

## 安装教程

- ### 环境及插件要求

   - Jdk8+
   - Mysql5.5+
   - Maven
   - Lombok<font color="red">（重要）</font>

- ### 导入项目

   - IntelliJ IDEA：Import Project -> Import Project from external model -> Maven
   - Eclipse：Import -> Exising Mavne Project


- ### 运行项目

  - 通过Java应用方式运行admin模块下的ltd.fdsa.BootApplication.java文件
  - 数据库配置：数据库名称timo   用户root    密码root
  - 访问地址：http://localhost:8080/
  - 默认帐号密码：admin/123456

## 使用说明

1. 使用文档：doc/使用文档.docx
2. 开发手册：[TIMO开发文档.在线](http://www.linln.cn/docs)
3. SQL文件：sdoc/timo.sql（经常忘记同步！）

演示地址： [http://www.linln.cn](http://www.linln.cn)
