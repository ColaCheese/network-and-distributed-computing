package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 定义User对象，User对象代表用户的各种属性，并实现序列化接口
 * 序列化的思想是“冻结”对象状态，然后写到磁盘或者在网络中传输(转成byte[])
 * 反序列化的思想是“解冻”对象状态，重新获得可用的 Java 对象
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class User implements Serializable {

    /** 用户名 */
    private String name;
    /** 密码 */
    private String password;
    /** 用户的消息列表 */
    private List<Message> messageList = new ArrayList<Message>();

    /**
     * 构造函数初始化User对象，有用户名、密码、消息列表三个属性
     *
     * @param name        用户名
     * @param password    密码
     * @param messageList 消息列表
     */
    public User(String name, String password, List<Message> messageList) {
        super();
        this.name = name;
        this.password = password;
        this.messageList = messageList;
    }

    /**
     * 构造函数初始化User对象，有用户名、密码两个属性
     *
     * @param name     用户名
     * @param password 密码
     */
    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    /**
     * 构造函数初始化User对象，有用户名一个属性
     *
     * @param name 用户名
     */
    public User(String name) {
        super();
        this.name = name;
    }

    /**
     * 获得用户名
     *
     * @return 用户名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户名
     *
     * @param name 用户名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获得用户的消息列表
     *
     * @return 消息列表
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * 设置用户的消息列表
     *
     * @param messageList 消息列表
     */
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * 复写toString方法，用于打印User信息
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[ name: " + name + " ]";
    }
}
