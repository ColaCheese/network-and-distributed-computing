package bean;

import java.io.Serializable;
import java.util.Date;


/**
 * 定义Message对象，Message对象代表消息的各种属性，并实现序列化接口
 * 序列化的思想是“冻结”对象状态，然后写到磁盘或者在网络中传输(转成byte[])
 * 反序列化的思想是“解冻”对象状态，重新获得可用的 Java 对象
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class Message implements Serializable {

    /** 评论者的名字 */
    private String commenter;
    /** 评论的日期 */
    private Date commentDate;
    /** 评论的内容 */
    private String contents;

    /**
     * 构造函数初始化Message对象
     */
    public Message(String commenter, Date commentDate, String contents) {
        super();
        this.commenter = commenter;
        this.commentDate = commentDate;
        this.contents = contents;
    }

    /**
     * 获得评论者的名字
     *
     * @return commenter 评论者的名字
     */
    public String getCommenter() {
        return commenter;
    }

    /**
     * 设置评论者的名字
     *
     * @param commenter 评论者的名字
     */
    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    /**
     * 获得评论的日期
     *
     * @return commentDate 评论的日期
     */
    public Date getCommentDate() {
        return commentDate;
    }

    /**
     * 设置评论的日期
     *
     * @param commentDate 评论的日期
     */
    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    /**
     * 获得评论的内容
     *
     * @return contents 评论的内容
     */
    public String getContents() {
        return contents;
    }

    /**
     * 设置评论的内容
     *
     * @param contents 评论的内容
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * 复写toString方法，用于打印Message信息
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Message [ Comment User = " + commenter + ", Comment Date = " + commentDate + ", Contents = " + contents + " ]";
    }
}
