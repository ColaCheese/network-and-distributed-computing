package bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Information used to represent the project
 *
 * @author Liu Yuyang
 *  * @version 1.0
 */
public class MyProject {

    /** 开始时间 */
    private Date beginDate;
    /** 结束时间 */
    private Date endDate;
    /** 项目的ID */
    private String id;

    /**
     * 构造函数
     *
     * @param id        项目的ID
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @throws ParseException 解析时间的异常
     */
    public MyProject(String id, String beginDate, String endDate) throws ParseException {
        //将String转换为Date类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.beginDate = sdf.parse(beginDate);
        this.endDate = sdf.parse(endDate);
        this.id = id;
    }

    /**
     * 获取项目的开始时间
     *
     * @return 项目的开始时间
     */
    public Date getProjectBeginDate() {
        return this.beginDate;
    }

    /**
     * 获取项目的结束时间
     *
     * @return 项目的结束时间
     */
    public Date getProjectEndDate() {
        return this.endDate;
    }

    /**
     * 获取项目的编号
     *
     * @return 项目的编号
     */
    public String getProjectID() {
        return this.id;
    }

    /**
     * 判断项目是否相同
     *
     * @param project 要判断的项目
     * @return 判断项目是否相同
     */
    public boolean equalProject(MyProject project) {
        if (project.getProjectBeginDate().before(beginDate) && project.getProjectEndDate().after(endDate)) {
            return true;
        } else {
            return false;
        }
    }
}
