import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class <em>Server</em> is a class representing server-side.
 *
 * @author Liu Yuyang.
 * @version 1.0.
 */
public class Server {

    /** 定义服务器的监听端口号 */
    private final int PORT = 80;

    /** 定义单个处理器线程池同时工作线程数目 */
    private final int POOLSIZE = 4;

    /** 定义服务器端的套接字 */
    private ServerSocket serverSocket;

    /** 定义线程池 */
    private ExecutorService executorService;

    /**
     * 创建服务器端套接字并绑定到指定的监听端口以及创建固定大小的线程池
     *
     * @throws IOException IOException
     */
    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * POOLSIZE);
    }

    /**
     * 启动服务器端
     *
     * @param args args
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
        new Server().service();
    }

    /**
     * 等待客户端的request请求
     */
    public void service() {
        Socket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
                executorService.execute(new Handler(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
