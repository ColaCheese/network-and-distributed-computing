package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import user.User;
import meeting.Meeting;

/**
 * 远程接口必须扩展接口java.rmi.Remote
 * 所有参数和返回类型必须序列化(因为要网络传输)
 * 任意远程对象都必须实现此接口
 * 只有远程接口中指定的方法可以被调用
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public interface Interface extends Remote {

    /** 所有方法必须抛出RemoteException */

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 代表是否创建成功
     * @throws RemoteException RemoteException
     */
    public int register(String username, String password) throws RemoteException;

    /**
     * 添加新会议
     *
     * @param meeting 会议对象
     * @param username 用户名
     * @param password 密码
     * @return 代表是否添加成功
     * @throws RemoteException RemoteException
     */
    public int add(Meeting meeting, String username, String password) throws RemoteException;

    /**
     * 查询相应时间段内是否有会议
     *
     * @param username 用户名
     * @param password 密码
     * @param start 开始时间
     * @param end 结束时间
     * @return 代表是否有会议
     * @throws RemoteException RemoteException
     */
    public ArrayList<Meeting> query(String username, String password, Date start, Date end) throws RemoteException;

    /**
     * 删除用户发起的某一个会议
     *
     * @param username 用户名
     * @param password 密码
     * @param meetingId 会议编号
     * @return 代表删除是否成功
     * @throws RemoteException RemoteException
     */
    public int delete(String username, String password, int meetingId) throws RemoteException;

    /**
     * 清除用户发起的所有会议
     *
     * @param username 用户名
     * @param password 密码
     * @return 代表清除是否成功
     * @throws RemoteException RemoteException
     */
    public int clear(String username, String password) throws RemoteException;
}