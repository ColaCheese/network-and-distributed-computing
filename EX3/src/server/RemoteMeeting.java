package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import interfaces.Interface;
import user.User;
import meeting.Meeting;

/**
 * 扩展UnicastRemoteObject类，并实现远程接口Interface
 * 服务器端实现远程接口。
 * 必须继承UnicastRemoteObject，以允许JVM创建远程的存根/代理。
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class RemoteMeeting extends UnicastRemoteObject implements Interface {

    /**
     * 服务器和客户端这个字段必须保持一致才能进行反序列化
     * JAVA序列化的机制是通过判断类的serialVersionUID来验证的版本一致的
     * 在进行反序列化时JVM会把传来的字节流中的serialVersionUID于本地相应实体类的serialVersionUID进行比较
     * 如果相同说明是一致的，可以进行反序列化，否则会出现反序列化版本一致的异常
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Meeting> meetings = new ArrayList<Meeting>();

    Comparator<Meeting> comparator = new Comparator<Meeting>() {
        @Override
        public int compare(Meeting s1, Meeting s2) {
            // 按照开始时间的先后排序
            return s1.getStart().compareTo(s2.getStart());
        }
    };

    /**
     * 必须定义构造方法，即使是默认构造方法，也必须把它明确地写出来，因为它必须抛出RemoteException异常
     */
    public RemoteMeeting() throws RemoteException {

    }

    /** 远程接口方法的实现 */

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 代表是否创建成功
     * @throws RemoteException RemoteException
     */
    @Override
    public int register(String username, String password) throws RemoteException {
        int order = 1;

        // 用户列表不为空，查找是否有重名
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    // 有重名的用户，返回-1
                    order = -1;
                }
            }
        }

        // 用户列表为空或者无重名的用户，正常创建，返回1
        if (users.isEmpty() || order != -1) {
            User user = new User(username, password);
            users.add(user);
        }

        return order;
    }

    /**
     * 添加新会议
     *
     * @param meeting 会议对象
     * @param username 用户名
     * @param password 密码
     * @return 代表是否添加成功
     * @throws RemoteException RemoteException
     */
    @Override
    public int add(Meeting meeting, String username, String password) throws RemoteException {
        int order = 0;

        // 密码账户匹配
        if (isUser(username, password) != -1) {
            order = 1;
            for (String name : meeting.getOtherUser()) {
                if (isUsers(name) == -1) {
                    // 会议的其它参与者不存在，返回-1
                    order = -1;
                }
            }
        }

        // 检查会议时间是否冲突
        if (order == 1) {
            if (isConflict(meeting.getStart(), meeting.getEnd(), isUser(username, password))) {
                order = 2;
                for (String name : meeting.getOtherUser()) {
                    // 检查和会议的其它参与者时间是否冲突
                    if (!isConflict(meeting.getStart(), meeting.getEnd(), isUsers(name))) {
                        order = -2;
                    }
                }

                if (order == 2) {
                    // 当前meetings编号的最大值
                    int t = 0;
                    for (Meeting meet1 : meetings) {
                        if (t <= meet1.getMeetingId()){
                            t = meet1.getMeetingId();
                        }
                    }

                    // 将新添加的meeting加入到meetings列表中，参会者添加meeting
                    meeting.setMeetingId(t + 1);
                    users.get(isUsers(username)).getMeetings().add(meeting);
                    for (String name : meeting.getOtherUser()) {
                        users.get(isUsers(name)).getMeetings().add(meeting);
                    }
                    meetings.add(meeting);
                }
            }
        }

        return order;
    }

    /**
     * 查询相应时间段内是否有会议
     *
     * @param username 用户名
     * @param password 密码
     * @param start 开始时间
     * @param end 结束时间
     * @return 会议的列表
     * @throws RemoteException RemoteException
     */
    @Override
    public ArrayList<Meeting> query(String username, String password, Date start, Date end) throws RemoteException {
        int order = 0;

        ArrayList<Meeting> meetings2 = new ArrayList<Meeting>();
        if (isUser(username, password) != -1) {
            order = 1;

            // 选取在开始和结束时间内的会议
            for (Meeting meeting : users.get((isUser(username, password))).getMeetings()) {
                if (meeting.getStart().compareTo(start) > -1 && meeting.getEnd().compareTo(end) < 1) {
                    meetings2.add(meeting);
                }
            }
        }

        if (order == 1) {
            // 按升序排列
            Collections.sort(meetings2, comparator);
            return meetings2;
        }

        // 没有就返回空值
        return null;
    }

    /**
     * 删除用户发起的某一个会议
     *
     * @param username 用户名
     * @param password 密码
     * @param meetingId 会议编号
     * @return 代表删除是否成功
     * @throws RemoteException RemoteException
     */
    @Override
    public int delete(String username, String password, int meetingId) throws RemoteException {
        if (isUser(username, password) != -1) {
            for (Meeting meeting : meetings) {
                if (meeting.getMeetingId() == meetingId) {
                    if (meeting.getSponser().equals(username)) {
                        idUserDelete(meetingId, meeting.getSponser());
                        idMeetDelete(meetingId);
                        return 1;
                    }
                    // 不是发起人
                    return -1;
                }
            }
            // 没有meeting
            return -2;
        }
        // 密码不正确
        return 0;
    }

    /**
     * 删除用户发起的所有会议
     *
     * @param username 用户名
     * @param password 密码
     * @return 代表删除是否成功
     * @throws RemoteException RemoteException
     */
    @Override
    public int clear(String username, String password) throws RemoteException {
        if (isUser(username, password) != -1) {
            // 存放该用户的所有发起的会议编号
            int A[] = new int[100];
            int i = 0;
            for (Meeting meeting : users.get(isUser(username, password)).getMeetings()) {
                if (meeting.getSponser().equals(username)) {
                    A[i] = meeting.getMeetingId();

                    // 打印编号
                    System.out.println(A[i]);
                    i++;
                }

            }
            for (i = 0; A[i] != 0; i++) {
                idMeetDelete(A[i]);
                idUserDelete(A[i], username);
            }
            return 1;
        }
        return 0;
    }

    /**
     * 判断此username是否存在用户列表中
     *
     * @param username 用户名
     * @return 代表是否在用户列表中存在或者第几个存在
     */
    private int isUsers(String username) {
        int i = 0;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return i;
            }
            i++;
        }
        // 不存在返回-1
        return -1;
    }

    /**
     * 判断账号密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 代表用户名和密码是否匹配或者第几个用户匹配
     */
    private int isUser(String username, String password) {
        int i = 0;
        for (User user : users) {
            if ((user.getUsername().equals(username))&&(user.getPassword().equals(password))) {
                return i;
            }
            i++;
        }
        // 不匹配返回-1
        return -1;
    }

    /**
     * 判断是否时间冲突，采用比较器的交集方法判断
     *
     * @param start 开始时间
     * @param end 结束时间
     * @param i 第几个用户
     * @return 时间是否冲突
     */
    private boolean isConflict(Date start, Date end, int i) {
        if (!users.get(i).getMeetings().isEmpty()) {
            for (Meeting meeting : users.get(i).getMeetings()) {
                if ((end.compareTo(meeting.getStart()) == 1) && (start.compareTo(meeting.getEnd()) == -1)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 删除某个人的某个id的会议
     *
     * @param id 会议编号
     * @param username 用户名
     */
    private void idUserDelete(int id, String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                int i = 1;
                for (Meeting meeting : user.getMeetings()) {
                    if (meeting.getMeetingId() == id) {
                        user.getMeetings().remove(i - 1);
                        break;
                    }
                    i++;
                }
            }
        }
    }

    /**
     * 删除会议列表的某个id的会议
     */
    private void idMeetDelete(int id) {
        int i = 1;
        for (Meeting meeting : meetings) {
            if (meeting.getMeetingId() == id) {
                for (String name : meeting.getOtherUser()) {
                    // 将该会议所有人的列表进行移除
                    idUserDelete(id, name);
                }

                // 移除该会议
                meetings.remove(i - 1);
                break;
            }
            i++;
        }
    }
}