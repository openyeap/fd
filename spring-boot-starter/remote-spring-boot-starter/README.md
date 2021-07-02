# Spring RPC

## RPC方式

1. RMI

   基于RMI协议，使用java的序列化机制，客户端服务端都必须时java，RMI协议不被防火墙支持，只能在内网使用

2. Hessian

   基于HTTP协议，使用自身的序列化机制，客户端服务端可以是不同的语言，HTTP协议被防火墙支持，可被外网访问

3. HttpInvoker

   基于HTTP协议，使用java的序列化机制，客户端服务端都必须时java，必须使用spring，HTTP协议被防火墙支持，可被外网访问

## 我们的方式

基于Hessian实现远程服务开放与远程调用
