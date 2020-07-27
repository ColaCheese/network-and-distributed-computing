package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import interfaces.HelloInterface;


/**
 * RMI Server
 * 创建HelloInterface实例并在rmiregistry中注册
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class HelloServer {

    /**
     * 启动 RMI 注册服务并进行对象注册
     */
    public static void main(String[] args) {
        try {

            // 启动RMI注册服务，指定端口为1099　（1099为默认端口）
            // 注册远程对象,向客户端提供远程对象服务。
            // 远程对象是在远程服务上创建的，无法确切地知道远程服务器上的对象的名称，
            // 但是,将远程对象注册到RMI Registry之后,
            // 客户端就可以通过RMI Registry请求到该远程服务对象的stub，
            // 利用stub代理就可以访问远程服务对象了。
            LocateRegistry.createRegistry(1099);

            // 创建远程对象的一个或多个实例，下面是hello对象
            // 可以用不同名字注册不同的实例
            HelloInterface hello = new Hello();

            // 把hello注册到RMI注册服务器上，命名为Hello
            // 如果要把hello实例注册到另一台启动了RMI注册服务的机器上，Naming.rebind("//192.168.1.105:1099/Hello",hello)
            Naming.rebind("Hello", hello);

            // 打印服务器就绪信息
            System.out.println("Hello Server is ready.");

        } catch (Exception e) {
            System.out.println("Hello Server failed: " + e);
        }
    }
}



