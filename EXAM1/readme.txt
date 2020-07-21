代理服务器主要是ProxyServer.java，ProxyHandler.java，ProxyClient.java三个java文件构成

ProxyServer.java：是对代理服务器初始化、启动，控制多线程等
ProxyHandler.java：是接收请求的客户端的报文，并进行改造
ProxyClient.java：是作为代理服务器对外界交互的客户端，向外界发送GET报文，并将响应发送给请求的客户端

客户端是Client.java，HttpClient.java两个文件构成，基本上是实验2的客户端稍加修改，去掉了PUT方法，以及字符编码从ios-8859-1变为UTF-8
以便适应大部分网页的编码