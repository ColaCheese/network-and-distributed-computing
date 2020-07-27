import org.omg.IOP.Encoding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 初始化线程并执行相关操作
 *
 * @author Liu Yuyang.
 * @version 1.0.
 */
public class Handler implements Runnable {

    /** 定义写入服务器端的字节流 */
    private BufferedInputStream istream = null;

    /** 定义从服务器端输出的字节流 */
    private BufferedOutputStream ostream = null;

    /** 定义报文头部（线程安全的字符串） */
    private StringBuffer header = null;

    /** 定义存放数据的缓存字节数组 */
    private byte[] buffer;

    /** 定义最大的缓存字节数为8192 */
    private static int buffer_size = 8192;

    /** 定义套接字 */
    private Socket socket;

    /** 定义CRLF回车换行符 */
    static private String CRLF = "\r\n";

    /** 定义服务器端资源存放路径 */
    static private String serverPath = "D:\\Test\\Server\\";

    /**
     * 根据sockets初始化线程
     *
     * @param socket socket
     */
    public Handler(Socket socket) {
        this.socket = socket;
        buffer = new byte[buffer_size];
        header = new StringBuffer();
        try {
            istream = new BufferedInputStream(socket.getInputStream());
            ostream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        // 是否为报文头部
        boolean isHeader = true;

        // 存放报文每一行，并组成字符串数组
        ArrayList<String> reqs = new ArrayList<String>();

        // 根据HttpClient部分处理报文头部的代码修改
        int last = 0, ch = 0;
        try {
            // 判断报文头部不为空且还没有读完报文头部
            while (isHeader && ((ch = istream.read()) != -1)) {
                switch (ch) {
                    case '\r':
                        break;
                    case '\n':
                        if (ch == last) {
                            isHeader = false;
                            break;
                        }
                        last = ch;
                        header.append('\n');
                        // 添加报文头部的一行
                        reqs.add(header.toString());
                        header = new StringBuffer();
                        break;
                    default:
                        last = ch;
                        header.append((char) ch);
                        break;
                }
            }

            // 判断PUT报文格式是否正确
            boolean formatJudge = true;
            // 定义要被储存到服务器的文件
            File newFile = null;
            // 定义要被储存到服务器的文件长度
            long fileSize = 0;
            // 定义要保存的文件相对于服务器根目录的相对路径
            String storePath = "";

            // 判断报文是否正确，同时读取报文，判断操作
            for (String in : reqs) {
                // GET报文格式正确，但不是PUT报文，所以将formatJudge置为false，执行doGet方法
                if (in.startsWith("GET") || in.startsWith("get")) {
                    formatJudge = false;
                    doGet(in);
                    break;
                }

                // PUT方法则先判断格式，格式错误则通过formatJudge控制不进行处理
                else if (in.startsWith("PUT") || in.startsWith("put")) {
                    // 将PUT行按空白字符进行分割
                    String[] reqs1 = in.split("\\s");
                    // 如果PUT行字符串数量不等于3，则PUT请求格式错误
                    if (reqs1.length != 3) {
                        formatJudge = false;
                        // 返回 HTTP 400 错误的请求
                        String response = "HTTP/1.1 400 Bad Request" + CRLF + CRLF;
                        buffer = response.getBytes();
                        ostream.write(buffer, 0, response.length());
                        ostream.flush();
                        break;
                    } else {
                        // 如果PUT行字符串数量等于3，则PUT请求格式正确
                        storePath = reqs1[1];
                        // 根据文件相对路径拼接出文件的绝对路径，将newFile实例化
                        newFile = new File(serverPath + storePath);
                    }
                }

                // 还有一种报文头部行是Content-length字段,用trim()去除首尾空白字符，指定为10进制
                else if (in.startsWith("Content-length")) {
                    fileSize = Long.parseLong(in.split("\\s")[1].trim(), 10);
                }
            }

            // PUT报文正确再进行处理
            if (formatJudge) {
                // 文件路径存在
                if (newFile.exists()) {
                    if (fileSize == 0) {
                        // 文件存在，但是传过来的body为0，返回 HTTP 204 没有内容，即成功处理请求但是没有返回内容给客户端
                        String response = "HTTP/1.1 204 No Content" + CRLF;
                        response += "Server: " + "LyyHttpServer/1.1" + CRLF;
                        response += "Content-Location: " + storePath + CRLF + CRLF;
                        buffer = response.getBytes();
                        ostream.write(buffer, 0, response.length());
                        ostream.flush();
                    } else {
                        // 文件存在，传过来body不为0
                        FileOutputStream fileOut = new FileOutputStream(serverPath + storePath);

                        // 按字节写入到文件中
                        ArrayList<Byte> info = new ArrayList<Byte>();
                        int b = 0;
                        while ((b = istream.read()) != -1) {
                            info.add((byte) b);
                            if (info.size() == fileSize) {
                                break;
                            }
                        }
                        byte[] content = new byte[info.size()];
                        for (int i = 0; i < info.size(); i++) {
                            content[i] = info.get(i).byteValue();
                        }

                        fileOut.write(content);
                        fileOut.flush();
                        fileOut.close();

                        // 定义文件类型
                        String fileType = "";

                        // 获得文件类型并构建报文
                        String[] s = storePath.split("/");
                        if (s[s.length - 1].contains("jpg") || s[s.length - 1].contains("JPG")) {
                            fileType = "image/jpeg";
                        } else if (s[s.length - 1].contains("htm") || s[s.length - 1].contains("html")) {
                            fileType = "text/html";
                        } else if (s[s.length - 1].contains("txt")) {
                            fileType = "text/plain";
                        } else if (s[s.length - 1].contains("png") || s[s.length - 1].contains("PNG")) {
                            fileType = "image/png";
                        }

                        // 文件存在，并成功进行了更新，返回 HTTP 204 没有内容，即成功处理请求但是没有返回内容给客户端
                        String response = "HTTP/1.1 204 No Content" + CRLF;
                        response += "Server: " + "LyyHttpServer/1.1" + CRLF;
                        response += "Content-type: " + fileType + CRLF;
                        response += "Content-length: " + fileSize + CRLF;
                        response += "Content-Location: " + storePath + CRLF + CRLF;
                        buffer = response.getBytes();
                        ostream.write(buffer, 0, response.length());
                        ostream.flush();
                    }
                } else {
                    // 文件路径不存在
                    if (fileSize == 0) {
                        // 如果传过来的内容长度为0，且文件不存在，那么就创建，但不写入，HTTP 201 成功创建
                        if (createFile(serverPath + storePath)) {
                            // 创建成功返回的报文
                            String response = "HTTP/1.1 201 Created" + CRLF;
                            response += "Server: " + "LyyHttpServer/1.1" + CRLF;
                            response += "Content-Location: " + storePath + CRLF + CRLF;
                            buffer = response.getBytes();
                            ostream.write(buffer, 0, response.length());
                            ostream.flush();
                        } else {
                            // 创建失败返回的报文
                            String response = "HTTP/1.1 500 Internal Server Error" + CRLF + CRLF;
                            buffer = response.getBytes();
                            ostream.write(buffer, 0, response.length());
                            ostream.flush();
                        }
                    } else {
                        //  创建失败返回的报文
                        if (!createFile(serverPath + storePath)) {
                            String response = "HTTP/1.1 500 Internal Server Error" + CRLF + CRLF;
                            buffer = response.getBytes();
                            ostream.write(buffer, 0, response.length());
                            ostream.flush();
                        }

                        // 创建成功，操作同上方操作
                        if (newFile.exists()) {

                            // 文件存在，传过来body不为0
                            FileOutputStream fileOut = new FileOutputStream(serverPath + storePath);

                            // 按字节写入到文件中
                            ArrayList<Byte> info = new ArrayList<Byte>();
                            int b = 0;
                            while ((b = istream.read()) != -1) {
                                info.add((byte) b);
                                if (info.size() == fileSize) {
                                    break;
                                }
                            }
                            byte[] content = new byte[info.size()];
                            for (int i = 0; i < info.size(); i++) {
                                content[i] = info.get(i).byteValue();
                            }

                            fileOut.write(content);
                            fileOut.flush();
                            fileOut.close();

                            // 定义文件类型
                            String fileType = "";

                            // 获得文件类型并构建报文
                            String[] s = storePath.split("/");
                            if (s[s.length - 1].contains("jpg") || s[s.length - 1].contains("JPG")) {
                                fileType = "image/jpeg";
                            } else if (s[s.length - 1].contains("htm") || s[s.length - 1].contains("html") ) {
                                fileType = "text/html";
                            } else if (s[s.length - 1].contains("txt")) {
                                fileType = "text/plain";
                            } else if (s[s.length - 1].contains("png") || s[s.length - 1].contains("PNG")) {
                                fileType = "image/png";
                            }

                            // 返回 HTTP 201 成功创建，即成功创建新的依赖目录及资源文件
                            String response = "HTTP/1.1 201 Created" + CRLF;
                            response += "Server: " + "LyyHttpServer/1.1" + CRLF;
                            response += "Content-type: " + fileType + CRLF;
                            response += "Content-length: " + fileSize + CRLF;
                            response += "Content-Location: " + storePath + CRLF + CRLF;
                            buffer = response.getBytes();
                            ostream.write(buffer, 0, response.length());
                            ostream.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 用于创建文件的依赖路径
     *
     * @param filename filename
     * @return if create file successfully
     */
    private boolean createFile(String filename) {
        File file = new File(filename);

        // 文件名以分隔符结尾为非法文件名，创建失败
        if (filename.endsWith(File.separator)) {
            System.out.println("非法文件名");
            return false;
        }

        // 创建上层文件路径
        if (!file.getParentFile().exists()) {
            System.out.println("上层路径不存在");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("文件夹创建失败");
                return false;
            }
        }

        // 创建新的空文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建成功");
                return true;
            } else {
                System.out.println("创建失败");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 对GET请求进行处理
     *
     * @param in GET请求的报文
     * @throws IOException IOException
     */
    private void doGet(String in) throws IOException {
        // 分解GET请求报文
        String[] req = in.split("\\s");
        // 请求的文件相对于服务器根目录的路径
        String path = "";
        // 请求的文件
        File file = null;
        // 文件大小
        long fileSize = 0;
        // 文件类型
        String fileType = "";

        // 请求报文格式不正确 HTTP 400 错误的请求
        if (req.length != 3) {
            String response = "HTTP/1.1 400 Bad Request" + CRLF + CRLF;
            buffer = response.getBytes();
            ostream.write(buffer, 0, response.length());
            ostream.flush();
        } else {
            // 请求报文格式正确
            path = req[1];
            // 将服务器根路径和相对路径拼接得到绝对路径
            path = serverPath + path;
            // 根据绝对路径打开文件
            file = new File(path);
            // 如果文件存在 HTTP 200 成功处理请求
            if (file.exists()) {
                String[] s = path.split("/");
                if (s[s.length - 1].contains("jpg")) {
                    fileType = "image/jpeg";
                } else if (s[s.length - 1].contains("htm")) {
                    fileType = "text/html";
                } else if (s[s.length - 1].contains("txt")) {
                    fileType = "text/plain";
                } else if (s[s.length - 1].contains("png")) {
                    fileType = "image/png";
                } else if (s[s.length - 1].contains("JPG")) {
                    fileType = "image/JPEG";
                }
                fileSize = file.length();

                // 不用将图片嵌入的情况
                if(fileType != "text/html"){
                    // 将响应报文的头部写入
                    String response = "HTTP/1.1 200 OK" + CRLF;
                    response += "Server: " + "LyyHttpServer/1.1" + CRLF;
                    response += "Content-type: " + fileType + CRLF;
                    response += "Content-length: " + fileSize + CRLF;
                    response += "Content-Location: " + req[1] + CRLF + CRLF;
                    buffer = response.getBytes();
                    ostream.write(buffer, 0, buffer.length);
                    ostream.flush();

                    long num = file.length() / buffer_size + 1;
                    int j = 1;
                    FileInputStream fileIn = new FileInputStream(path);
                    while (j <= num) {
                        buffer = new byte[buffer_size];
                        // 从文件读入到缓存数组
                        fileIn.read(buffer);
                        // 从缓存数组写入到报文
                        ostream.write(buffer);
                        ostream.flush();
                        j++;
                    }
                    fileIn.close();
                }else{
                    // 需要将图片嵌入HTML文件
                    FileReader reader = new FileReader(file);
                    char[] deposit = new char[(int)fileSize];
                    while( reader.read(deposit) != -1 ){
                        // 将HTML内容存进字符串
                        String string = new String(deposit,0,deposit.length);

                        // 正则表达式找出img标签src属性
                        List<String> srcList = new ArrayList<String>();
                        //匹配字符串中的img标签
                        Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");
                        Matcher matcher = p.matcher(string);
                        boolean hasPic = matcher.find();
                        //判断是否含有图片
                        if(hasPic == true)
                        {
                            //如果含有图片，那么持续进行查找，直到匹配不到
                            while(hasPic)
                            {
                                //获取第二个分组的内容，也就是 (.*?)匹配到的
                                String group = matcher.group(2);
                                //匹配图片的地址
                                Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                                Matcher matcher2 = srcText.matcher(group);
                                if( matcher2.find() )
                                {
                                    //把获取到的图片地址添加到列表中
                                    srcList.add( matcher2.group(3) );
                                }
                                //判断是否还有img标签
                                hasPic = matcher.find();
                            }

                            String tmp="";

                            // 将图片转为base64格式
                            for(String src:srcList){
                                if(src.startsWith("http")){
                                    if(src.contains("jpg")){
                                        tmp = ImageToBase64.NetImageToBase64(src,"jpg");
                                    }else if(src.contains("png")){
                                        tmp = ImageToBase64.NetImageToBase64(src,"png");
                                    }
                                    string = string.replace(src,tmp);
                                }else {
                                    if(src.contains("jpg")){
                                        tmp = ImageToBase64.ImageToBase64(serverPath+src,"jpg");
                                    }else if(src.contains("png")){
                                        tmp = ImageToBase64.ImageToBase64(serverPath+src,"png");
                                    }
                                    string = string.replace(src,tmp);
                                }
                            }
                        }

                        buffer = string.getBytes();
                        fileSize = buffer.length;

                        // 将响应报文的头部写入
                        String response = "HTTP/1.1 200 OK" + CRLF;
                        response += "Server: " + "LyyHttpServer/1.1" + CRLF;
                        response += "Content-type: " + fileType + CRLF;
                        response += "Content-length: " + fileSize + CRLF;
                        response += "Content-Location: " + req[1] + CRLF + CRLF;
                        buffer = response.getBytes();
                        ostream.write(buffer, 0, buffer.length);
                        ostream.flush();

                        buffer = string.getBytes();
                        ostream.write(buffer,0,string.length());
                        ostream.flush();

                        System.out.println("已将图片嵌入到HTML文档");
                    }
                }
            } else {
                // 如果文件不存在 HTTP 404 找不到资源
                String response = "HTTP/1.1 404 NOT FOUND" + CRLF + CRLF;
                buffer = response.getBytes();
                ostream.write(buffer, 0, response.length());
                ostream.flush();
            }
        }
    }
}