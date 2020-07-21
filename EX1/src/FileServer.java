import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * server-side.
 *
 * @author Liu Yuyang.
 * @version 1.0.
 *
 */
public class FileServer {
    /** TCP连接端口 */
    private static final int TCP_PORT = 2021;
    /**  单个处理器线程池同时工作线程数目 */
    private static final int POOL_SIZE = 10;

    private ServerSocket serverSocket;
    private ExecutorService executorService;

    /**
     * Create a server-side socket and thread pool.
     *
     * @throws IOException IOException.
     *
     */
    private FileServer() throws IOException {
        // 创建服务器端套接字
        serverSocket = new ServerSocket(TCP_PORT);
        // 创建线程池
        // Runtime的availableProcessors()方法返回当前系统可用处理器的数目
        // 由JVM根据系统的情况来决定线程的数量
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOL_SIZE);
        System.out.println("服务器启动，线程池创建完成");
    }

    /**
     * Start the service.
     *
     * @param args args.
     * @throws IOException IOException.
     *
     */
    public static void main(String[] args) throws IOException {
        new FileServer().service();
    }

    /**
     * service method implements.
     *
     */
    private void service() {
        Socket socket = null;
        while (true) {
            try {
                // 等待用户连接
                socket = serverSocket.accept();
                // 把执行交给线程池来维护
                executorService.execute(new Handler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}