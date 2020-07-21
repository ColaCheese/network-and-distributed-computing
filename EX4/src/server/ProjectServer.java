package server;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import bean.*;

/**
 * Server Run this program to start Web Service
 *
 * @author Liu Yuyang
 * @version 1.0
 */
@WebService(name = "project", portName = "projectPort", targetNamespace = "http://www.webservice.com")
public class ProjectServer {

    /** 项目列表 */
    private HashMap<String, MyProject> projects = new HashMap<String, MyProject>();
    /** 用户列表 */
    private HashMap<String, Person> users = new HashMap<String, Person>();
    private HashMap<String, ArrayList<String>> person_projects = new HashMap<String, ArrayList<String>>();
    final String NEW_LINE = System.getProperty("line.separator");

    /**
     * @param name     用户名
     * @param passWard 密码
     * @return 登陆是否成功
     */
    @WebMethod
    public int login(String name, String passWard) {
        Person person = users.get(name);
        if (person == null) {
            return -1;
        }
        if (person.getPersonPassWard().equals(passWard)) {
            //登录成功
            return 1;
        }
        return 0;
    }


    /**
     * @param name     姓名
     * @param passWard 密码
     * @return 添加是否成功
     */
    @WebMethod
    public int addPerson(String name, String passWard) {
        Person newperson = null;
        newperson = users.get(name);
        if (newperson == null) {
            // 用户列表为空，直接添加
            newperson = new Person(name, passWard);
            if (users.put(passWard, newperson) != null) {
                ArrayList<String> projectlist = new ArrayList<String>();
                person_projects.put(name, projectlist);
                // 返回注册成功
                return 1;
            }
            // 系统有误
            return 0;
        }
        // 该用户已存在
        return -1;
    }

    /**
     * @param begin 开始时间
     * @param end   结束时间
     * @return 查询会议的结果
     * @throws ParseException ParseException
     */
    @WebMethod
    public String quaryMeeting(String begin, String end) throws ParseException {
        MyProject project = new MyProject(null, begin, end);
        String str = new String();
        Iterator ite = projects.keySet().iterator();
        while (ite.hasNext()) {
            MyProject poj = projects.get(ite.next());
            if (poj.equalProject(project)) {
                str += (poj.getProjectID() + " start:"
                        + poj.getProjectBeginDate().toString() + "  end:"
                        + poj.getProjectEndDate().toString() + NEW_LINE);
            }
        }
        return str;
    }

    /**
     * @param username 姓名
     * @param id       编号
     * @param start    开始时间
     * @param end      结束时间
     * @return 是否成功
     * @throws ParseException ParseException
     */
    @WebMethod
    public int addProjects(String username, String id, String start, String end)
            throws ParseException {
        if (projects.get(id) == null) {
            MyProject project = new MyProject(id, start, end);
            if (project.getProjectEndDate().before(project.getProjectBeginDate())) {
                return 0;
            }
            projects.put(id, project);
            ArrayList<String> userProjects = person_projects.get(username);
            if (userProjects == null) {
                userProjects = new ArrayList<String>();
            }
            userProjects.add(id);
            person_projects.put(username, userProjects);
            return 1;
        }
        return -1;
    }

    /**
     * @param username 用户名
     * @return 项目信息
     */
    @WebMethod
    public String displayProject(String username) {
        Person person = users.get(username);
        String str = new String();
        ArrayList<String> personmeetings = person_projects.get(username);
        for (int i = 0; i < personmeetings.size(); i++) {
            str += (personmeetings.get(i) + "  "
                    + projects.get(personmeetings.get(i)).getProjectID() + NEW_LINE);
        }
        return str;
    }

    /**
     * @param username   用户名
     * @param passward   密码
     * @param meeting_Id 编号
     * @return 是否成功
     */
    @WebMethod
    public int removeProject(String username, String passward, String meeting_Id) {
        Person person = users.get(username);
        if (/*person != null*/true) {
            if (projects.remove(meeting_Id) != null) {
                person_projects.get(username).remove(meeting_Id);
                return 1;
            }
        }
        return -1;
    }


    /**
     * @param username 项目名
     * @return 删除是否成功
     */
    @WebMethod
    public boolean removeAll(String username) {
        ArrayList<String> personmeetings = person_projects.get(username);
        for (int i = 0; i < personmeetings.size(); i++) {
            projects.remove(personmeetings.get(i));
        }
        personmeetings.clear();
        return true;
    }

    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:8001/WebMeeting/ProjectServer", new ProjectServer());
        System.out.println("服务器已经启动");
    }
}
