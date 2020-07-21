import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class <em>HttpClient</em> is a class representing a simple HTTP client.
 *
 * @author Liu Yuyang.
 * @version 1.0.
 */

public class HttpClient {

    /**
     * default HTTP port is port 80.
     */
    private static int port = 80;

    /**
     * Allow a maximum buffer size of 8192 bytes.
     */
    private static int buffer_size = 8192;

    /**
     * Response is stored in a byte array.
     */
    private byte[] buffer;

    /**
     * My socket to the world.
     */
    Socket socket = null;

    /**
     * Default port is 80.
     */
    private static final int PORT = 80;

    /**
     * Output stream to the socket.
     */
    BufferedOutputStream ostream = null;

    /**
     * Input stream from the socket.
     */
    BufferedInputStream istream = null;

    /**
     * StringBuffer storing the header.
     */
    private StringBuffer header = null;

    /**
     * StringBuffer storing the response.
     */
    private StringBuffer response = null;

    /**
     * String to represent the Carriage Return and Line Feed character sequence.
     */
    static private String CRLF = "\r\n";

    /**
     * String to represent the client path.
     */
    static private String clientPath = "D:\\Test\\Client\\";

    /**
     * HttpClient constructor.
     */
    public HttpClient() {
        buffer = new byte[buffer_size];
        header = new StringBuffer();
        response = new StringBuffer();
    }

    /**
     * <em>connect</em> connects to the input host on the default http port -- port
     * 80. This function opens the socket and creates the input and output streams
     * used for communication.
     */
    public void connect(String host) throws Exception {

        /** Open my socket to the specified host at the default port. */
        socket = new Socket(host, PORT);

        /** Create the output stream. */
        ostream = new BufferedOutputStream(socket.getOutputStream());

        /** Create the input stream. */
        istream = new BufferedInputStream(socket.getInputStream());
    }

    /**
     * <em>processGetRequest</em> process the input GET request.
     */
    public void processGetRequest(String request) throws Exception {
        /** Send the request to the server. */
        request += CRLF + CRLF;
        buffer = request.getBytes();
        ostream.write(buffer, 0, request.length());
        ostream.flush();

        /** waiting for the response. */
        processResponse();
    }

    /**
     * <em>processPutRequest</em> process the input PUT request.
     */
    public void processPutRequest(String request) throws Exception {
        // =======start your job here============//

        // 利用正则表达式，以空白字符（可能是空格、制表符、其他空白）为标志，拆分报文
        String[] requestSplit = request.split("\\s");
        // 定义客户端要上传的文件
        File file = null;
        // request的每一行要以CRLF结束
        request += CRLF;

        // 判断报文格式是否正确，正确长度应为3，示例“PUT /smile.jpg HTTP/1.0”
        if (requestSplit.length == 3) {
            // 拼接出文件路径
            String filename = requestSplit[1];
            file = new File(clientPath + filename);

            // 格式正确且找到文件
            if (file.exists()) {
                // 封装报文，加CRLF表示消息报头结束
                request += "Content-length: " + file.length() + CRLF + CRLF;
                buffer = request.getBytes();
                ostream.write(buffer, 0, buffer.length);

                // 获得报文的body
                FileInputStream fileInputStream = new FileInputStream(file);
                long num = file.length() / buffer_size + 1;
                int i = 1;
                while (i <= num) {
                    i++;
                    buffer = new byte[buffer_size];
                    fileInputStream.read(buffer);
                    ostream.write(buffer, 0, buffer.length);
                }
                fileInputStream.close();
                ostream.flush();

            } else {
                // 文件不存在则发送长度为0的body
                System.out.println("File do not exist!\n");
                request += "Content-length: 0" + CRLF + CRLF;
                buffer = request.getBytes();
                ostream.write(buffer, 0, buffer.length);
                ostream.flush();
            }

        } else {
            // 报文不完整也发送长度为0的body
            System.out.println("The message is incomplete!\n");
            request += "Content-length: 0" + CRLF + CRLF;
            buffer = request.getBytes();
            ostream.write(buffer, 0, buffer.length);
            ostream.flush();
        }

        /** waiting for the response. */
        processResponse();

        // =======end of your job============//
    }

    /**
     * <em>processResponse</em> process the server response.
     */
    public void processResponse() throws Exception {
        // last represents the last char that has been read, and c means current char being read
        int last = 0, ch = 0;

        /** Process the header and add it to the header StringBuffer. */
        // loop control
        boolean isHeader = true;
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
                    header.append("\n");
                    break;
                default:
                    last = ch;
                    header.append((char) ch);
            }
        }

        /** Read the contents and add it to the response StringBuffer. */
        while (istream.read(buffer) != -1) {
            response.append(new String(buffer, "iso-8859-1"));
            // may be need more new buffer
            buffer = new byte[buffer_size];
        }
    }

    /**
     * Get the response header.
     */
    public String getHeader() {
        return header.toString();
    }

    /**
     * Get the server's response.
     */
    public String getResponse() {
        return response.toString();
    }

    /**
     * Close all open connections -- sockets and streams.
     */
    public void close() throws Exception {
        socket.close();
        istream.close();
        ostream.close();
    }
}
