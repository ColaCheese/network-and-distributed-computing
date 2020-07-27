package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 * 远程接口必须扩展接口java.rmi.Remote
 * 所有参数和返回类型必须序列化(因为要网络传输)
 * 任意远程对象都必须实现此接口
 * 只有远程接口中指定的方法可以被调用
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public interface HelloInterface extends Remote {

    /** 所有方法必须抛出RemoteException */
    public String echo(String msg) throws RemoteException;

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册是否成功
     * @throws RemoteException RemoteException
     */
    public String register(String username, String password) throws RemoteException;

    /**
     * 显示所有注册用户
     *
     * @return 所有注册用户名列表
     * @throws RemoteException RemoteException
     */
    public String showusers() throws RemoteException;

    /**
     * 显示用户所有留言
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户所有留言列表
     * @throws RemoteException RemoteException
     */
    public String checkmessages(String username, String password) throws RemoteException;

    /**
     * 留言
     *
     * @param username     用户名
     * @param password     密码
     * @param receiverName 接收者
     * @param messageText  留言信息
     * @return 留言返回消息
     * @throws RemoteException RemoteException
     */
    public String leavemessage(String username, String password, String receiverName, String messageText) throws RemoteException;
}
