# Fast Data Switcher

Fast Data Switcher是一个异构的数据交换工具，致力于使用一个工具解决不同数据源（JDBC、FILE、Kafka、FTP等）之间数据交换的问题。

Fast Data Switcher在设计上采用“框架+插件”的结构，具有较好的扩展性，框架相当于数据缓冲区，插件则为访问不同的数据源提供实现。



【特性】

1. 跨平台独立运行
2. WebUI界面操作
3. 异构数据源之间交换
4. 基于插件模式，易扩展



【设计】

1. 配置文件：使用json格式配置datasource、pipline、dataTarget的参数
2. 插件模式：数据读取、处理和数据写入均基于插件实现
3. 事件监听模式: 数据的交换使用高性能环形数据缓冲区实现调整、异步的数据交换



