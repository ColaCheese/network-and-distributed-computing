package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import interfaces.Interface;
import meeting.Meeting;

/**
 * RMI Client Side
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class RMIClient {
    public static void main(String[] argv) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        try {
            // 访问远程的meet对象
            Interface meet = (Interface) Naming.lookup("Meet");

            // 从控制台进行操作
            while (true) {
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                String request = keyboard.readLine();

                String[] reqs = request.split(" ");

                if (reqs[0].equals("register")) {

                    if (reqs.length == 3) {
                        int i = meet.register(reqs[1], reqs[2]);

                        // i==1注册失败
                        if (i == -1) {
                            System.out.println("repeat username, register failed");
                        } else {
                            System.out.println("successfully !");
                        }

                        System.out.println("RMI Menu:");
                        System.out.println("register [username] [password]:");
                        System.out.println("add [username] [password] [otherusername] [start] [end] [title]:");
                        System.out.println("query [username] [password] [start] [end]:");
                        System.out.println("delete [username] [password] [meetingid]");
                        System.out.println("clear [username] [password]");
                        System.out.println("please input option:");
                    }else {
                        System.out.println("wrong cmd");
                    }

                } else if (reqs[0].equals("add")) {

                    // 参会者人数可能比较多所以大于等于7
                    if (reqs.length >= 7) {
                        // 获取起止时间
                        Date start = new Date();
                        Date end = new Date();
                        start = dateFormat.parse(reqs[reqs.length - 3]);
                        end = dateFormat.parse(reqs[reqs.length - 2]);

                        // 获取其它的参会者
                        ArrayList<String> othername = new ArrayList<String>();
                        for (int i = 3; i < reqs.length - 3; i++) {
                            othername.add(reqs[i]);
                        }

                        // 生成Meeting对象
                        Meeting meeting = new Meeting(0, start, end, reqs[reqs.length - 1], reqs[1], othername);

                        meeting.setEnd(end);
                        meeting.setStart(start);
                        meeting.setOtherUser(othername);
                        meeting.setSponser(reqs[1]);
                        meeting.setTitle(reqs[reqs.length - 1]);

                        int i = meet.add(meeting, reqs[1], reqs[2]);

                        if (i == 2) {
                            System.out.println("successfully !");
                        } else if (i == 1) {
                            System.out.println("failed: sponser time conflict");
                        } else if (i == -1) {
                            System.out.println("failed: other username do not exist");
                        } else if (i == 0) {
                            System.out.println("failed: username do not match password");
                        } else if (i == -2) {
                            System.out.println("failed: other user time conflict");
                        } else {
                            System.out.println("failed: unknow wrong");
                        }
                    }else {
                        System.out.println("wrong cmd");
                    }

                } else if (reqs[0].equals("query")) {

                    if (reqs.length == 5) {
                        Date start = new Date();
                        Date end = new Date();
                        start = dateFormat.parse(reqs[3]);
                        end = dateFormat.parse(reqs[4]);

                        ArrayList<Meeting> meetingList = meet.query(reqs[1], reqs[2], start, end);

                        if (meetingList == null) {
                            System.out.println("failed: username do not match password");
                        } else {
                            if (meetingList.isEmpty()) {
                                System.out.println("there is no such meeting");
                            } else {
                                for (Meeting meeting : meetingList) {
                                    System.out.println(meeting);
                                }
                            }
                        }
                    } else {
                        System.out.println("wrong cmd");
                    }

                } else if (reqs[0].equals("delete")) {

                    if (reqs.length == 4) {
                        int i = meet.delete(reqs[1], reqs[2], Integer.parseInt(reqs[3]));
                        if (i == 1) {
                            System.out.println("successfully !");
                        } else if (i == -1) {
                            System.out.println("failed: you didn't creat this meeting");
                        } else if (i == -2) {
                            System.out.println("failed: this is no meeting with this id");
                        } else if (i == 0){
                            System.out.println("failed: username do not match password");
                        } else{
                            System.out.println("failed: unknow wrong");
                        }
                    } else {
                        System.out.println("wrong cmd");
                    }

                } else if (reqs[0].equals("clear")) {

                    if (reqs.length == 3) {
                        int i = meet.clear(reqs[1], reqs[2]);
                        if (i == 0) {
                            System.out.println("failed: username do not match password");
                        } else {
                            System.out.println("successfully !");
                        }
                    } else {
                        System.out.println("wrong cmd");
                    }




                    // start with java




                } else if (reqs[0].equals("java")) {
                    if (reqs.length == 7) {

                        if (reqs[4].equals("register")) {

                            int i = meet.register(reqs[5], reqs[6]);

                            // i==1注册失败
                            if (i == -1) {
                                System.out.println("repeat username, register failed");
                            } else {
                                System.out.println("successfully !");
                            }

                            System.out.println("RMI Menu:");
                            System.out.println("register [username] [password]:");
                            System.out.println("add [username] [password] [otherusername] [start] [end] [title]:");
                            System.out.println("query [username] [password] [start] [end]:");
                            System.out.println("delete [username] [password] [meetingid]");
                            System.out.println("clear [username] [password]");
                            System.out.println("please input option:");

                        } else if (reqs[4].equals("clear")) {

                            int i = meet.clear(reqs[5], reqs[6]);
                            if (i == 0) {
                                System.out.println("failed: username do not match password");
                            } else {
                                System.out.println("successfully !");
                            }
                        } else {
                            System.out.println("wrong cmd");
                        }

                    } else if (reqs.length >= 11) {

                        if (reqs[4].equals("add")) {

                            Date start = new Date();
                            Date end = new Date();
                            start = dateFormat.parse(reqs[reqs.length - 3]);
                            end = dateFormat.parse(reqs[reqs.length - 2]);

                            ArrayList<String> othername = new ArrayList<String>();

                            for (int i = 7; i < reqs.length - 3; i++) {
                                othername.add(reqs[i]);
                            }

                            Meeting meeting = new Meeting(0, start, end, reqs[reqs.length - 1], reqs[5], othername);

                            meeting.setEnd(end);
                            meeting.setStart(start);
                            meeting.setOtherUser(othername);
                            meeting.setSponser(reqs[5]);
                            meeting.setTitle(reqs[10]);

                            int i = meet.add(meeting, reqs[5], reqs[6]);

                            if (i == 2) {
                                System.out.println("successfully !");
                            } else if (i == 1) {
                                System.out.println("failed: sponser time conflict");
                            } else if (i == -1) {
                                System.out.println("failed: other username do not exist");
                            } else if (i == 0) {
                                System.out.println("failed: username do not match password");
                            } else if (i == -2) {
                                System.out.println("failed: other user time conflict");
                            } else {
                                System.out.println("failed: unknow wrong");
                            }
                        }

                    } else if (reqs.length == 9) {
                        if (reqs[4].equals("query")) {
                            Date start = new Date();
                            Date end = new Date();
                            start = dateFormat.parse(reqs[7]);
                            end = dateFormat.parse(reqs[8]);

                            ArrayList<Meeting> meetingList = meet.query(reqs[5], reqs[6], start, end);

                            if (meetingList == null) {
                                System.out.println("failed: username do not match password");
                            } else {
                                if (meetingList.isEmpty()) {
                                    System.out.println("there is no such meeting");
                                } else {
                                    for (Meeting meeting : meetingList) {
                                        System.out.println(meeting);
                                    }
                                }
                            }
                        }
                    } else if (reqs.length == 8) {
                        if (reqs[4].equals("delete")) {
                            int i = meet.delete(reqs[5], reqs[6], Integer.parseInt(reqs[7]));

                            if (i == 1) {
                                System.out.println("successfully !");
                            } else if (i == -1) {
                                System.out.println("failed: you didn't creat this meeting");
                            } else if (i == -2) {
                                System.out.println("failed: this is no meeting with this id");
                            } else if (i == 0){
                                System.out.println("failed: username do not match password");
                            } else{
                                System.out.println("failed: unknow wrong");
                            }
                        }
                    } else {
                        System.out.println("wrong cmd");
                    }
                } else if (reqs[0].equals("end")){
                    break;
                } else {
                    System.out.println("wrong cmd");
                }
            }
        } catch (Exception e) {
            System.out.println("Meet Client exception: " + e);
        }
    }
}