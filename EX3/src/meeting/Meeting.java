package meeting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 定义Meeting对象，并实现序列化接口
 * 序列化的思想是“冻结”对象状态，然后写到磁盘或者在网络中传输(转成byte[])
 * 反序列化的思想是“解冻”对象状态，重新获得可用的 Java 对象
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class Meeting implements Serializable {
    /** Meeting的编号 */
    private int meetingId;
    /** Meeting的开始时间 */
    private Date start;
    /** Meeting的结束时间 */
    private Date end;
    /** Meeting的标题 */
    private String title;
    /** Meeting的发起者 */
    private String sponser;
    /** Meeting的其它参与者 */
    private ArrayList<String> otherUser = new ArrayList<String>();

    /**
     * 构造函数对Meeting对象进行初始化
     */
    public Meeting() {
        super();
    }

    /**
     * 构造函数对Meeting对象进行初始化
     */
    public Meeting(int meetingId, Date start, Date end, String title, String sponser, ArrayList<String> otherUser) {
        super();
        this.meetingId = meetingId;
        this.start = start;
        this.end = end;
        this.title = title;
        this.sponser = sponser;
        this.otherUser = otherUser;
    }

    /**
     * @return the meetingId
     */
    public int getMeetingId() {
        return this.meetingId;
    }

    /**
     * @param meetingId the meetingId to set
     */
    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    /**
     * @return the start
     */
    public Date getStart() {
        return this.start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return this.end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the sponser
     */
    public String getSponser() {
        return this.sponser;
    }

    /**
     * @param sponser the sponser to set
     */
    public void setSponser(String sponser) {
        this.sponser = sponser;
    }

    /**
     * @return the otherUser
     */
    public ArrayList<String> getOtherUser() {
        return otherUser;
    }

    /**
     * @param otherUser the otherUser to set
     */
    public void setOtherUser(ArrayList<String> otherUser) {
        this.otherUser = otherUser;
    }

    @Override
    public String toString() {
        return "[ meetID: " + meetingId + ", start time: " + start + ", end time: " + end + ",title: " + title + ",people: " + sponser + " " + otherUser + "]";
    }
}