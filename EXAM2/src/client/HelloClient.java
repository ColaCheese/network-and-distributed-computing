package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.util.StringTokenizer;

import interfaces.HelloInterface;


/**
 * RMI Client Side
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class HelloClient {

    public static void main(String[] argv) {

        try {
            // 客户端
            String clientName = null;
            // 服务器
            String serverName = null;
            // 端口号
            int portNumber = 0;
            // 接受用户信息的输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            // 创建远程对象
            String infoString = in.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(infoString);
            if (stringTokenizer.hasMoreTokens()) {
                stringTokenizer.nextToken();
                clientName = stringTokenizer.nextToken();
                serverName = stringTokenizer.nextToken();
                portNumber = Integer.parseInt(stringTokenizer.nextToken());
            }

            if (!clientName.equals("HelloClient")) {
                System.out.println("客户端错误");
            } else if (!serverName.equals("localhost")) {
                System.out.println("服务器错误");
            } else if (portNumber != 1099) {
                System.out.println("端口号错误");
            } else {

                // 通过查找获得远程对象
                HelloInterface hello = (HelloInterface) Naming.lookup("Hello");

                // 调用远程方法
                System.out.println(hello.echo("good morning"));

                // 存储用户输入信息
                String info = null;
                while ((info = in.readLine()) != null) {

                    // 注册服务
                    if (info.startsWith("register")) {
                        StringTokenizer stringTokenizer2 = new StringTokenizer(info);

                        if (stringTokenizer2.countTokens() != 3) {
                            System.err.println("请输入正确的格式!");
                        } else {
                            System.out.println("*****用户注册******");
                            String method = stringTokenizer2.nextToken();
                            String username = stringTokenizer2.nextToken();
                            String password = stringTokenizer2.nextToken();
                            // 判断是否注册成功，并返回响应的提示
                            System.out.println(hello.register(username, password));
                        }
                    } else if (info.startsWith("showusers")) {
                        System.out.println("*****显示用户*****");
                        System.out.println(hello.showusers());
                    } else if (info.startsWith("checkmessages")) {
                        // 查看消息
                        System.out.println("*****查看消息*****");
                        StringTokenizer stringTokenizer4 = new StringTokenizer(info);

                        if (stringTokenizer4.countTokens() != 3) {
                            System.err.println("请输入正确的格式的格式!");
                        } else {
                            String method = stringTokenizer4.nextToken();
                            String username = stringTokenizer4.nextToken();
                            String password = stringTokenizer4.nextToken();
                            System.out.println(hello.checkmessages(username, password));
                        }
                    } else if (info.startsWith("leavemessage")) {
                        // 留言
                        System.out.println("*****留言*****");
                        StringTokenizer stringTokenizer5 = new StringTokenizer(info);

                        if (stringTokenizer5.countTokens() != 5) {
                            System.err.println("请输入正确的格式!");
                        } else {
                            String method = stringTokenizer5.nextToken();
                            String username = stringTokenizer5.nextToken();
                            String password = stringTokenizer5.nextToken();
                            String receiverName = stringTokenizer5.nextToken();
                            String messageText = stringTokenizer5.nextToken();

                            if (username.equals(receiverName)) {
                                System.out.println("请选择其他用户传递消息");
                            } else {
                                System.out.println(hello.leavemessage(username, password, receiverName, messageText));
                            }
                        }
                    } else if (info.equals("quit")) {
                        //退出
                        System.out.println("*****再见*****");
                        break;
                    } else if (info.equals("help")) {
                        //帮助
                        System.out.println(hello.echo(""));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("HelloClient exception: " + e);
        }
    }
}
