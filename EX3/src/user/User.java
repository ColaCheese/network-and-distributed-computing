package user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import meeting.Meeting;

/**
 * 定义User对象，并实现序列化接口
 * 序列化的思想是“冻结”对象状态，然后写到磁盘或者在网络中传输(转成byte[])
 * 反序列化的思想是“解冻”对象状态，重新获得可用的 Java 对象
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class User implements Serializable {
    /** User的用户名 */
    private String username;
    /** User的密码 */
    private String password;
    /** User参与的Meeting列表 */
    private List<Meeting> meetings = new ArrayList<Meeting>();

    /**
     * 构造函数对User对象进行初始化
     */
    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the meetings
     */
    public List<Meeting> getMeetings() {
        return this.meetings;
    }

    /**
     * @param meeting the meetings to add
     */
    public void addMeeting(Meeting meeting) {
        this.meetings.add(meeting);
    }
}