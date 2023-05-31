## 文件结构

0 - 基于内存 MemoryTable
1 - ./data/wal/{0-n}.wal
2 - ./data/lsm/{topic}/{0-n}.lsm
3 - ./data/idx/{topic}.ix|.ts

## 监控

## JMX

JMX即Java管理扩展，可以通过MBean的注册来实现运行时监控，而MXBean是一种可以支持复杂变量类型的MBean

通过JMX，我们可以监控的内容包括：

1、服务器中各种资源的使用情况：如CPU、内存等

2、JVM内存使用情况

3、JVM中的线程情况

4、JVM中加载的类


## Micrometer
Micrometer为最流行的监控系统提供了一个简单的仪表客户端外观，允许仪表化JVM应用，而无需关心是哪个供应商提供的指标。
### Meter

Micrometer提供了与供应商无关的Meter，包括：

- timers（计时器）
- Gauges（量规）
- Counters（计数器）
- DistributionSummaries（分布式摘要）
- LongTaskTimers（长任务定时器）
- FunctionCounter
- FunctionTimer
- TimeGauge。

它具有维度数据模型，当与维度监视系统结合使用时，可以高效地访问特定的命名度量，并能够跨维度深入研究。


