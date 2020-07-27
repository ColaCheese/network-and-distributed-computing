import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.Socket;

/**
 * 代理服务器的客户端，用于向外部服务器发送请求并接收响应报文
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class ProxyClient {
    /** 定义用于缓存数据的字节数组的大小 */
    private static int buffer_size = 8192;

    /** 定义报文中用于对报文进行分割的CRLF标志 */
    private static String CRLF = "\r\n";

    /** 定义用于缓存数据的字节数组 */
    private byte[] buffer;

    /** 定义储存报文头的字符串 */
    private StringBuffer header = null;

    /** 定义储存报文响应内容的字符串 */
    private StringBuffer response = null;

    /** 定义连接外部服务器的socket */
    private Socket proxySocket = null;

    /** 定义代理服务器客户端的输入输出流 */
    BufferedOutputStream ostream = null;
    BufferedInputStream istream = null;

    /**
     * 代理服务器客户端的构造函数，并初始化
     */
    public ProxyClient() {
        buffer = new byte[buffer_size];
        header = new StringBuffer();
        response = new StringBuffer();
    }

    /**
     * 连接到指定的主机和相应的端口号
     *
     * @param host 主机地址
     * @param port 连接端口号
     */
    public void connect(String host, int port) throws Exception {

        // 实例化socket连接
        proxySocket = new Socket(host, port);

        // 创建输出流
        ostream = new BufferedOutputStream(proxySocket.getOutputStream());

        // 创建输出流
        istream = new BufferedInputStream(proxySocket.getInputStream());
    }

    /**
     * 发送GET报文
     *
     * @param request GET请求行
     * @param host 主机地址
     * @throws Exception Exception
     */
    public void processGetRequest(String request, String host) throws Exception {
        request += CRLF;
        request += "Host: " + host + CRLF;

        // 取消长连接
        request += "Connection: Close" + CRLF + CRLF;

        buffer = request.getBytes();
        ostream.write(buffer, 0, request.length());
        ostream.flush();

        // 等待响应
        processResponse();
    }

    /**
     * 处理响应
     *
     * @throws Exception Exception
     */
    public void processResponse() throws Exception {
        int last = 0, c = 0;
        // 处理响应头并存入字符串中，根据实验二代码改编
        boolean inHeader = true;
        while (inHeader && ((c = istream.read()) != -1)) {
            switch (c) {
                case '\r':
                    break;
                case '\n':
                    if (c == last) {
                        inHeader = false;
                        break;
                    }
                    last = c;
                    header.append("\n");
                    break;
                default:
                    last = c;
                    header.append((char) c);
            }
        }

        // 把字节数组读入到响应内容的字符串中，编码采用UTF-8
        while (istream.read(buffer) != -1) {
            response.append(new String(buffer, "UTF-8"));
            buffer = new byte[buffer_size];
        }
    }

    /**
     * 获取响应头部
     */
    public String getHeader() {
        return header.toString();
    }

    /**
     * 获取响应主体
     */
    public String getResponse() {
        return response.toString();
    }

    /**
     * 关闭socket和输入输出流
     */
    public void close() throws Exception {
        proxySocket.close();
        istream.close();
        ostream.close();
    }
}