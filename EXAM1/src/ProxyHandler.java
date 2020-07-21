import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;

/**
 * 线程池具体的维护、处理方法
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class ProxyHandler implements Runnable {
    /** 定义接收的客户端socket */
    private Socket socket;

    /** 定义代理服务器的客户端 */
    private ProxyClient proxyClient;

    /** 定义用于缓存数据的字节数组 */
    private byte[] buffer;

    /** 定义用于缓存数据的字节数组的大小 */
    private static int buffer_size = 8192;

    /** 定义报文中用于对报文进行分割的CRLF标志 */
    private static String CRLF = "\r\n";

    /** 定义服务器端的输入输出流 */
    BufferedReader reader = null;
    BufferedOutputStream ostream = null;

    /**
     * 初始化服务器端的输入输出流
     *
     * @throws IOException IOException
     */
    public void initStream() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ostream = new BufferedOutputStream(socket.getOutputStream());
    }

    /**
     * 构造函数，初始化线程池具体的维护、处理方法
     */
    public ProxyHandler(Socket socket) {
        this.socket = socket;

        // 对缓存区进行刷新
        buffer = new byte[buffer_size];

        this.proxyClient = new ProxyClient();
    }

    /**
     * 定义run方法，具体的处理逻辑
     */
    @Override
    public void run() {
        try {
            // 执行输入输出流的初始化方法
            initStream();

            // 报文的一行
            String info = null;

            // 对报文进行处理
            while ((info = reader.readLine()) != null) {
                if (info.toLowerCase().startsWith("get")) {
                    // GET请求行
                    doGet(info);
                } else if (info.toLowerCase().startsWith("from") || info.toLowerCase()
                        .startsWith("user-agent")) {
                    // From或者User-Agent行，不做处理
                } else {
                    // 其它，错误的请求行
                    badRequest();
                    break;
                }
            }
        } catch (Exception e) {
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
     * 错误的请求返回 HTTP 400
     *
     * @throws IOException IOException
     */
    private void badRequest() throws IOException {
        String response = "HTTP/1.0 400 Bad Request" + CRLF + CRLF;
        buffer = response.getBytes();
        ostream.write(buffer, 0, response.length());
        ostream.flush();
    }

    /**
     * 实现报文中的GET请求方法
     *
     * @param info GET请求行字符串
     * @return 是否GET请求被成功执行
     * @throws Exception Exception
     */
    private boolean doGet(String info) throws Exception {
        URL url = null;

        // 将GET请求行按空格分割
        String[] orders = info.split(" ");

        if (!(orders.length == 3)) {
            // 分割后的字符串数不为3，请求错误
            badRequest();
            return false;
        } else {
            // 根据url字符串创建URL对象
            url = new URL(orders[1]);

            // 向url指定的网页发送请求
            requestGet(url);

            // 打印出请求的主机地址
            System.out.println("Get to " + url.getHost());

            //
            responseGet();
        }
        return true;
    }


    /**
     * 根据url发送GET请求
     *
     * @param url url
     * @throws Exception Exception
     */
    private void requestGet(URL url) throws Exception {
        // 默认去连接80端口，否则连接url对象里面指定的端口
        proxyClient.connect(url.getHost(), url.getPort() == -1 ? 80 : url.getPort());
        String request = "GET " + url.getFile() + " HTTP/1.1";
        proxyClient.processGetRequest(request, url.getHost());
    }

    /**
     * 从代理服务器的客户端接收响应，并把报文发送到请求的客户端
     *
     * @throws IOException IOException
     */
    private void responseGet() throws IOException {
        String header = proxyClient.getHeader() + "\n";
        String body = proxyClient.getResponse();
        buffer = header.getBytes();

        ostream.write(buffer, 0, header.length());
        ostream.write(body.getBytes());
        ostream.flush();
    }

}