import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * client-side.
 *
 * @author Liu Yuyang.
 * @version 1.0.
 *
 */
public class FileClient {
    /** TCP连接端口 */
    private static final int TCP_PORT = 2021;
    /** UDP端口 */
    private static final int UDP_PORT = 2020;
    /** 一次传送文件的字节数 */
    private static final int SEND_SIZE = 1024;

    private Socket socket;
    private DatagramSocket datagramSocket;

    /**
     * Create a client socket.
     *
     * @throws UnknownHostException UnknownHostException.
     * @throws IOException IOException.
     *
     */
    private FileClient() throws UnknownHostException, IOException {
        System.out.println("Please Input HOST");
        Scanner hostScanner = new Scanner(System.in);
        while (hostScanner.next() == null){
            System.out.println("Please Input HOST");
        }
        String HOST = hostScanner.nextLine();
        System.out.println("OK");

        socket = new Socket(HOST, TCP_PORT);
    }

    /**
     * call the send() method, which will send that message on the specified queue.
     *
     * @param args args.
     * @throws UnknownHostException UnknownHostException.
     * @throws IOException IOException.
     *
     */
    public static void main(String[] args) throws UnknownHostException, IOException {
        new FileClient().send();
    }

    /**
     * send method implements
     *
     */
    private void send() {
        try {
            // 客户端输出流，向服务器发消息（https://blog.csdn.net/m0_37574389/article/details/84024689）
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // 客户端输入流，接收服务器消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 装饰输出流，自动刷新（https://blog.csdn.net/qq_38977097/article/details/80967896）
            PrintWriter pw = new PrintWriter(bw, true);

            // 输出服务器返回连接成功的消息
            System.out.println(br.readLine());

            // 接受用户信息
            Scanner in = new Scanner(System.in);
            String cmd;
            while ((cmd = in.next()) != null) {
                // 发送给服务器端
                pw.println(cmd);
                if (cmd.equals("cd") || cmd.equals("get")) {
                    String dir = in.next();
                    pw.println(dir);
                    // 下载文件
                    if (cmd.equals("get")) {
                        long fileLength = Long.parseLong(br.readLine());
                        if (fileLength != -1) {
                            System.out.println("文件大小为：" + fileLength);
                            getFile(dir, fileLength);
                        } else {
                            System.out.println("Unknown file");
                        }
                    }
                }
                String msg = null;
                while ((msg = br.readLine()) != null) {
                    if (msg.equals("Cmd End")) {
                        break;
                    }
                    // 输出服务器返回的消息
                    System.out.println(msg);
                }

                if (cmd.equals("bye")) {
                    System.out.println("断开连接，客户端运行完毕");
                    break;
                }
            }
            in.close();
            br.close();
            bw.close();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    // 断开连接
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gets a file by name and length.
     *
     * @param fileName the name of the file that wants getting.
     * @param fileLength the length of the file that wants getting.
     * @throws IOException IOException.
     *
     */
    private void getFile(String fileName, long fileLength) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(new byte[SEND_SIZE], SEND_SIZE);
        // UDP连接
        datagramSocket = new DatagramSocket(UDP_PORT);
        byte[] recInfo = new byte[SEND_SIZE];
        //自定义文件存储位置
        Scanner scanner1 = new Scanner(System.in);
        //先输入盘
        System.out.println("盘>>：");
        String pan = scanner1.nextLine();
        Scanner scanner2 = new Scanner(System.in);
        //再输入文件夹
        System.out.println("文件夹>>");
        String file = scanner2.nextLine();
        String root = pan + ":\\" + file + "\\";
        //判断文件名是否存在
        File rootFile = new File(root);
        if (!rootFile.exists()) {
            System.out.println("Directory not exist!");
            datagramSocket.close();
            return;
        }
        if (!rootFile.isDirectory()) {
            System.out.println("This is not a directory!");
            datagramSocket.close();
            return;
        }
        System.out.println("开始接收文件：" + root);
        FileOutputStream fos = new FileOutputStream(new File((root) + fileName));
        int count = (int) (fileLength / SEND_SIZE) + ((fileLength % SEND_SIZE) == 0 ? 0 : 1);

        while ((count--) > 0) {
            // 接收文件信息
            datagramSocket.receive(datagramPacket);
            recInfo = datagramPacket.getData();
            fos.write(recInfo, 0, datagramPacket.getLength());
            fos.flush();
        }
        System.out.println("文件接收完毕");
        datagramSocket.close();
        fos.close();
    }
}