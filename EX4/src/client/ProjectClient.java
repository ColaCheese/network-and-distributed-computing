package client;

import java.util.Scanner;

import com.webservice.*;
import server.*;


/**
 * Client Side
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class ProjectClient {

    /** 换行符 */
    final static String NEW_LINE = System.getProperty("line.separator");

    /** 显示功能 以及输出参数的格式 */
    public static void display() {
        System.out.println("1 :添加项目 " + NEW_LINE
                + "    参数:<id> <start> <end>");
        System.out.println("2 :删除项目 " + NEW_LINE
                + "    参数: <项目编号>");
        System.out.println("3 :清空项目 " + NEW_LINE
                + "    参数:no args");
        System.out.println("4 :查询项目 " + NEW_LINE
                + "    参数:<开始时间> <结束时间>");
        System.out.println("5 :帮助 " + NEW_LINE
                + "    没有参数");
        System.out.println("6 :退出 " + NEW_LINE
                + "    MULL");
        System.out.println("请输入操作编号:");
    }

    /**
     * run()
     */
    public static void run() {
        try {
            //用于存放用户名
            String username;
            //用于存放密码
            String passWard;
            //一个服务器ProjectServerService实体
            ProjectServerService service = new ProjectServerService();
            //获取Project
            Project projectService = service.getProjectPort();
            {
                System.out.println("输入注册的用户名和密码:");
                Scanner input = new Scanner(System.in);
                //读取用户名
                username = input.next();
                //读取密码
                passWard = input.next();
                //注册用户并且返回状态码
                int i = projectService.addPerson(username, passWard);
                //成功注册
                if (i == 0) {
                    System.out.println("成功");
                    display();
                }
                //注册失败
                if (i == 1) {
                    System.out.println("失败");
                    run();
                }
                //用户已经存在
                if (i == -1) {
                    System.out.println("用户已存在");
                    run();
                }
            }

            while (true) {

                Scanner io = new Scanner(System.in);
                int key = Integer.parseInt(io.next());
                switch (key) {
                    //添加项目
                    case 1:
                        try {
                            System.out
                                    .println("请输入编号 , 开始时间和结束时间 "
                                            + NEW_LINE + "时间格式 :2019-12-28 14:00");
                            Scanner input = new Scanner(System.in);
                            //代办事项的ID
                            String id = input.next();
                            //开始内容
                            String startdata = input.next();
                            //开始时间
                            String starttime = input.next();
                            //结束内容
                            String enddata = input.next();
                            //结束时间
                            String endtime = input.next();

                            //添加任务
                            int i = projectService.addProjects(username, id,
                                    startdata + " " + starttime, enddata + " "
                                            + endtime);

                            //成功添加
                            if (i == 1) {
                                System.out.println("成功");
                                break;
                            }
                            //时间有错误
                            if (i == 0) {
                                System.out.println("失败，时间冲突");
                                break;
                            }
                            //项目已经存在
                            if (i == -1) {
                                System.out.println("失败，该项目已经存在");
                                break;
                            }
                        } catch (ParseException_Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        //删除事项
                    case 2: {
                        System.out.println(projectService.displayProject(username));
                        System.out.println("请输入编号");
                        Scanner input = new Scanner(System.in);
                        String project_id = input.next();
                        int i = projectService.removeProject(username,
                                passWard, project_id);
                        if (i == 1) {
                            System.out.println("删除成功");
                            break;
                        }
                        if (i == -1) {
                            System.out.println("删除失败");
                            break;
                        }
                    }

                    //清除事项
                    case 3: {
                        if (projectService.removeAll(username)) {
                            System.out.println("清除成功");
                            break;
                        } else {
                            System.out.println("清除失败");
                            break;
                        }
                    }

                    //查询事项
                    case 4:
                        try {
                            System.out.println("请输入开始和结束时间");
                            Scanner input = new Scanner(System.in);
                            String startdata = input.next();
                            String starttime = input.next();
                            String enddata = input.next();
                            String endtime = input.next();


                            System.out.println(projectService.quaryMeeting(startdata + " " + starttime, enddata + " "
                                    + endtime));

                            break;

                        } catch (ParseException_Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    case 5:
                        break;
                    case 6:
                        return;

                    default:
                        System.out.println("请填写一个编号 ");
                        break;
                }
                display();
            }
        }
        //处理异常
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            //输出
            System.out.println(e.toString());
            return;
        }

    }

    public ProjectClient() {
    }

    public static void main(String[] args) {
        //启动
        ProjectClient.run();
    }
}