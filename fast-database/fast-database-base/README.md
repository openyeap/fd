### 简介

原生数据库的操作支持，参考spring framework data relational的sql和query构建。支持利用java编写sql语句。

支持以下数据库：

mysql
postgre
mssql
sysbase

支持数据库的：

query
update
insert
delete
create table

### 代码结构

前端显示controller中定义了基本结构
数据存储repository中定义数据库的各种通用操作，按读写分离方式实现
业务逻辑service中定义业务上各种通用的基于数据存储操作