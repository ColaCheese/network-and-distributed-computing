import java.io.*;
import java.net.*;
import java.util.Scanner;


/**
 * The thread responsible for communicating with a single customer.
 *
 * @author Liu Yuyang.
 * @version 1.0.
 *
 */
public class Handler implements Runnable {
    /** 连接地址 */
    private static final String HOST = "127.0.0.1";
    /** UDP端口 */
    private static final int UDP_PORT = 2020;
    /** 一次传送文件的字节数 */
    private static final int SEND_SIZE = 1024;

    private Socket socket;
    private DatagramSocket datagramSocket;
    private SocketAddress socketAddress;

    BufferedReader br;
    BufferedWriter bw;
    PrintWriter pw;
    private final String rootPath = "C:\\Users\\leo yuya\\Desktop\\Resource";
    public static String currentPath = "C:\\Users\\leo yuya\\Desktop\\Resource";

    /**
     * create a socket.
     *
     * @param socket create connection.
     *
     */
    public Handler(Socket socket) {
        this.socket = socket;
    }

    /**
     * Initializes the input/output stream object method.
     *
     * @throws IOException IOException.
     *
     */
    public void initStream() throws IOException {
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        pw = new PrintWriter(bw, true);
    }

    /**
     * Override run method to do with different cmd.
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        System.out.println("Please Input HOST");
        Scanner hostScanner = new Scanner(System.in);
        while (hostScanner.next() == null){
            System.out.println("Please Input HOST");
        }
        String HOST = hostScanner.nextLine();
        System.out.println("OK");

        try {
            // 服务器信息
            System.out.println(socket.getInetAddress() + ":" + socket.getPort() + ">连接成功");
            // 初始化输入输出流对象
            initStream();
            // 向客户端发送连接成功信息
            pw.println(socket.getInetAddress() + ":" + socket.getPort() + ">连接成功");

            String info;
            while (null != (info = br.readLine())) {
                // 退出
                if (info.equals("bye")) {
                    break;
                } else {
                    switch (info) {
                        //服务器返回当前目录文件列表
                        case "ls":
                            listDir(currentPath);
                            break;
                        //进入指定目录
                        case "cd":
                            String dir = null;
                            if (null != (dir = br.readLine())) {
                                moveDir(dir);
                            } else {
                                pw.println("please input a direction after cd");
                            }
                            break;
                        //返回上级目录
                        case "cd..":
                            backDir();
                            break;
                        //通过UDP下载指定文件
                        case "get":
                            String fileName = br.readLine();
                            sendFile(fileName);
                            break;
                        default:
                            pw.println("unknown cmd");
                    }
                    // 用于标识目前的指令结束，以帮助跳出Client的输出循环
                    pw.println("Cmd End");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (null != socket) {
                try {
                    br.close();
                    bw.close();
                    pw.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sends a file by fileName.
     *
     * @param fileName the fileName of the file that wants sending.
     * @throws SocketException SocketException.
     * @throws IOException IOException.
     * @throws InterruptedException InterruptedException.
     *
     */
    private void sendFile(String fileName) throws SocketException, IOException, InterruptedException {
        // 文件不存在
        if (!isFileExist(fileName)) {
            pw.println(-1);
            return;
        }
        //得到文件路径
        File file = new File(currentPath + "\\" + fileName);
        pw.println(file.length());
        // UDP
        datagramSocket = new DatagramSocket();
        socketAddress = new InetSocketAddress(HOST, UDP_PORT);
        DatagramPacket datagramPacket;

        byte[] sendInfo = new byte[SEND_SIZE];
        int size = 0;
        datagramPacket = new DatagramPacket(sendInfo, sendInfo.length, socketAddress);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

        while ((size = bufferedInputStream.read(sendInfo)) > 0) {
            datagramPacket.setData(sendInfo);
            datagramSocket.send(datagramPacket);
            sendInfo = new byte[SEND_SIZE];
        }

        datagramSocket.close();
    }

    /**
     * Implement cd.. function, Back to upper direction.
     *
     */
    private void backDir() {
        if (currentPath.equals(rootPath)) {
            pw.println("The current path is the root path, cd.. cannot realize.");
        } else {
            for (int i = currentPath.length(); i > 0; i--) {
                if (currentPath.substring(i - 1, i).equals("\\")) {
                    currentPath = currentPath.substring(0, i - 1);
                    pw.println((new File(currentPath)).getName() + " > OK");
                    break;
                }
            }
        }
    }

    /**
     * Lists files and directions in the current path.
     *
     * @param currentPath the path wants listing.
     *
     */
    private void listDir(String currentPath) {
        File rootFile = new File(currentPath);
        File[] fileList = rootFile.listFiles();
        int MaxLength = 40;
        pw.println("Type" + "\t" + "Name" + addSpace(MaxLength - 4) + "Size");
        for (File file : fileList) {
            // 是文件
            if (file.isFile()) {
                pw.println("<file>" + "\t" + file.getName() + addSpace(MaxLength - file.getName().length()) + file.length() + "B");
                // 是文件夹
            } else if (file.isDirectory()) {
                pw.println("<dir>" + "\t" + file.getName() + addSpace(MaxLength - file.getName().length()) + file.length() + "B");
            }
        }
    }

    /**
     * Aligns prints by adding space.
     *
     * @param count the count of space needs printing.
     * @return the space.
     *
     */
    public static String addSpace(int count) {
        String str = "";
        for (int i = 0; i < count; i++) {
            str += " ";
        }
        return str;
    }

    /**
     * Changes direction into dir.
     *
     * @param dir the direction wants changing into.
     *
     */
    private void moveDir(String dir) {
        // 初始设定目录不存在
        Boolean isExist = false;
        // 初始设定是文件夹
        Boolean isDir = true;
        File rootFile = new File(currentPath);
        File[] fileList = rootFile.listFiles();
        for (File file : fileList) {
            // 找到了同名的文件夹或文件
            if (file.getName().equals(dir)) {
                isExist = true;
                // 名字对应文件夹 或 名字对应文件
                if (file.isDirectory()) {
                    isDir = true;
                    break;
                } else {
                    isDir = false;
                    pw.println("You cannot cd file, only direction admitted");
                }
            }
        }
        // 是文件夹并且存在 或 文件夹不在当前目录
        if (isExist && isDir) {
            currentPath = currentPath + "\\" + dir;
            pw.println(dir + " > OK");
        } else if (isDir && (!isExist)) {
            pw.println(dir + " direction not exist!");
        }

    }

    /**
     * Judges if file exists in current path.
     *
     * @param fileName the name of file needs judging.
     * @return if file exits in current path.
     *
     */
    public static boolean isFileExist(String fileName) {
        //假设文件不存在于当前目录
        boolean isExist = false;
        File rootFile = new File(currentPath);
        File[] fileList = rootFile.listFiles();

        for (File file:fileList){
            if (file.getName().equals(fileName) && file.isFile()){
                isExist = true;
            }
        }
        return isExist;
    }
}