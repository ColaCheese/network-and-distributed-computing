package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import interfaces.HelloInterface;
import bean.Message;
import bean.User;


/**
 * 扩展UnicastRemoteObject类，并实现远程接口HelloInterface
 * 服务器端实现远程接口。
 * 必须继承UnicastRemoteObject，以允许JVM创建远程的存根/代理。
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class Hello extends UnicastRemoteObject implements HelloInterface {

    /**
     * 服务器和客户端这个字段必须保持一致才能进行反序列化
     * JAVA序列化的机制是通过判断类的serialVersionUID来验证的版本一致的
     * 在进行反序列化时JVM会把传来的字节流中的serialVersionUID于本地相应实体类的serialVersionUID进行比较
     * 如果相同说明是一致的，可以进行反序列化，否则会出现反序列化版本一致的异常
     */
    private static final long serialVersionUID = 1L;

    /** 定义状态 */
    private String status = "0000";

    /** 定义用户列表 */
    private List<User> userList = new ArrayList<User>();

    /**
     * 必须定义构造方法，即使是默认构造方法，也必须把它明确地写出来，因为它必须抛出出RemoteException异常
     */
    public Hello() throws RemoteException {

    }

    /** 远程接口方法的实现 */

    /**
     * 返回菜单信息
     *
     * @see interfaces.HelloInterface#echo(String)
     */
    @Override
    public String echo(String msg) throws RemoteException {
        System.out.println("Called by HelloClient");
        status = status + msg;

        //返回菜单
        return "********** Distributed Message Center **********" + "\r\n" + "(1) Show registered users ---- arguments:<username><password>" + "\r\n"
                + "(2) Register a new user ---- arguments:none" + "\r\n" + "(3) Check Messages ---- arguments:<username><password>"
                + "\r\n" + "(4) Leave a message ---- arguments:<username><password><receiver_name><message_text>" + "\r\n"
                + "(5) Help ---- arguments:none" + "\r\n"
                + "(6) Quit ---- arguments:none" + "\r\n"
                + "************************************************" + "\r\n"
                + "Enter choice: " + "\r\n";
    }

    /**
     * 用户注册
     *
     * @see interfaces.HelloInterface#register(String, String)
     */
    @Override
    public String register(String username, String password) throws RemoteException {

        // 定义消息列表
        List<Message> messageList = new ArrayList<Message>();

        int flag = 0;
        for (int i = 0; i < userList.size(); i++) {
            if (username.equals(userList.get(i).getName())) {
                flag++;
            }
        }

        // 用户已存在
        if (flag > 0) {
            System.out.println(username + "用户名已经存在，请输入新的用户名");
            return username + "用户名已经存在，请输入新的用户名";
        } else {

            // 没有该用户
            User user = new User(username, password, messageList);
            userList.add(user);
            System.out.println(username + "注册成功");
            return username + "注册成功";
        }
    }

    /**
     * 显示用户
     *
     * @see interfaces.HelloInterface#showusers()
     */
    @Override
    public String showusers() throws RemoteException {
        // 用于保存注册用户信息
        String msgString = "";

        // 如果没有用户，则给出提示
        if (userList.size() == 0) {
            return "没有任何用户";
        } else {
            for (int i = 0; i < userList.size(); i++) {
                System.out.println(userList.get(i).getName());
                msgString += userList.get(i).getName() + "\r\n";
            }
            return msgString;
        }
    }

    /**
     * 查看消息
     *
     * @see interfaces.HelloInterface#checkmessages(String, String)
     */
    @Override
    public String checkmessages(String username, String password) throws RemoteException {
        // 用于保存未注册用户信息,密码错误提示等信息。
        String msg = "";
        // 用于保存用户留言信息
        String msg1 = "";
        // 若flag1>0，用户已注册
        int flag1 = 0;
        // 若flag2>0，用户密码错误
        int flag2 = 0;
        for (int i = 0; i < userList.size(); i++) {

            if (userList.get(i).getName().equals(username)) {
                flag1++;
                if (userList.get(i).getPassword().equals(password)) {
                    System.out.println("用户已注册，密码正确");
                } else {
                    System.out.println("用户已注册，密码错误");
                    msg += "用户已注册，密码错误" + "\r\n";
                    // 用户已注册，密码错误
                    flag2++;
                }
            }
        }

        //用户已注册
        if (flag1 > 0) {
            System.out.println("用户已注册");
        } else {//用户未注册
            System.out.println("用户未注册");
            msg += "用户未注册" + "\r\n";
        }

        // 如果用户名密码存在正确
        if (flag1 > 0 && flag2 == 0) {
            for (int i = 0; i < userList.size(); i++) {

                if (userList.get(i).getName().equals(username)) {

                    if (userList.get(i).getMessageList().size() == 0) {
                        System.out.println("当前消息列表为空");
                        msg1 += "当前消息列表为空" + "\r\n";
                    } else {
                        for (int j = 0; j < userList.get(i).getMessageList().size(); j++) {

                            System.out.println(userList.get(i).getMessageList().get(j).toString());
                            msg1 += userList.get(i).getMessageList().get(j).toString() + "\r\n";
                        }

                    }
                }
            }
            return msg1;
        } else {
            return msg;
        }
    }

    /**
     * 留言
     *
     * @see interfaces.HelloInterface#leavemessage(String, String, String, String)
     */
    @Override
    public String leavemessage(String username, String password, String receiverName, String messageText) throws RemoteException {
        // 用于保存未注册用户信息,密码错误提示等信息
        String msg = "";
        // 用于保存成功留言信息
        String msg1 = "";
        // 若flag1>0，用户已注册
        int flag1 = 0;
        // 若flag2>0，用户密码错误
        int flag2 = 0;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(username)) {
                flag1++;
                if (userList.get(i).getPassword().equals(password)) {
                    System.out.println("用户已注册，密码正确");
                } else {
                    System.out.println("用户已注册，密码错误");
                    msg += "用户已注册，密码错误" + "\r\n";
                    // 用户已注册，密码错误
                    flag2++;
                }
            }
        }

        if (flag1 > 0) {
            System.out.println("用户已注册");
        } else {
            System.out.println("用户未注册");
            msg += "用户未注册" + "\r\n";
        }

        // 若flag3>0，表示找到收件人
        int flag3 = 0;

        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equals(receiverName)) {
                System.out.println("找到收件人");
                flag3++;
            }
        }

        if (flag3 == 0) {
            System.out.println("找不到收件人");
            msg += "找不到收件人" + "\r\n";
        }

        // 如果用户名密码存在正确,找到收件人
        if (flag1 > 0 && flag2 == 0 && flag3 > 0) {

            for (int i = 0; i < userList.size(); i++) {

                if (userList.get(i).getName().equals(receiverName)) {
                    Date currentDate = new Date();
                    Message message = new Message(username, currentDate, messageText);
                    userList.get(i).getMessageList().add(message);
                    msg1 += username + "给" + receiverName + "留言成功:" + messageText;
                }
            }
            return msg1;
        } else {
            return msg;
        }
    }

    /**
     * 获取状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
