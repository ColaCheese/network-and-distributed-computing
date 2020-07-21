import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定义代理服务器，用于从客户端接收请求并进行处理
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class ProxyServer {
    /** 定义代理服务器的socket */
    ServerSocket serverSocket;

    /** 定义代理服务器监听的tcp端口号 */
    private final int PORT = 8000;

    /** 定义每个处理器的核心的线程数量 */
    private final int POOLSIZE = 4;

    /** 定义线程池 */
    ExecutorService executorService;

    /**
     * 构造函数，初始化代理服务器
     */
    public ProxyServer() throws IOException {
        // 用监听端口号PORT实例化socket
        serverSocket = new ServerSocket(PORT);

        // 创建线程池，并用当前可使用的处理器核心数*4来定义整个线程池可用的线程数量
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);

        // 初始化成功
        System.out.println("The proxy server is now handing the request");
    }

    /**
     * 代理服务器的运行方法
     */
    public void service() {
        Socket socket = null;
        while (true) {
            try {
                // 等待客户端连接
                socket = serverSocket.accept();
                // 把执行交给线程池维护
                executorService.execute(new ProxyHandler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException {
        // 启动代理服务器
        new ProxyServer().service();
    }
}