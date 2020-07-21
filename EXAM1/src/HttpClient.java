import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * Class <em>HttpClient</em> is a class representing a simple HTTP client.
 *
 * @author Liu Yuyang.
 * @version 1.0.
 */

public class HttpClient {

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
     * Default port is 8000.
     */
    private static final int PORT = 8000;

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
     * HttpClient constructor;
     */
    public HttpClient() {
        buffer = new byte[buffer_size];
        header = new StringBuffer();
        response = new StringBuffer();
    }

    /**
     * <em>connect</em> Connects to the input host on the default http port.
     * Connects to the input host on the port Open sockets and create input
     * and output streams for communication
     */
    public void connect(String host) throws Exception {
        // Open my socket to the specified host at the default port.
        socket = new Socket(host, PORT);
        // Create the output stream.
        ostream = new BufferedOutputStream(socket.getOutputStream());
        // Create the input stream.
        istream = new BufferedInputStream(socket.getInputStream());
    }

    /**
     * <em>processGetRequest</em> process the input GET request.
     */
    public void processGetRequest(String request, String host) throws Exception {
        request += CRLF;
        request += "Host: " + host + CRLF;

        // Long connection blocking
        request += "Connection: Close" + CRLF + CRLF;
        buffer = request.getBytes();
        ostream.write(buffer, 0, request.length());
        ostream.flush();

        // waiting for the response.
        processResponse();
    }

    /**
     * Get the contents of the file
     *
     * @param saveFile Files that need to be saved by the server
     * @throws Exception Exception
     */
    private void getFileBody(File saveFile) throws Exception {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(saveFile));
        while ((bufferedInputStream.read(buffer)) > 0) {
            ostream.write(buffer, 0, buffer.length);
            buffer = new byte[buffer_size];
        }
        bufferedInputStream.close();
    }

    /**
     * Process the server response.
     */
    public void processResponse() throws Exception {
        int last = 0, c = 0;
        // Process the header and add it to the header StringBuffer.
        boolean inHeader = true; // loop control
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

        // Read the contents and add it to the response StringBuffer.
        while (istream.read(buffer) != -1) {
            response.append(new String(buffer, "UTF-8"));
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
     * Close all open connections including sockets and streams.
     */
    public void close() throws Exception {
        socket.close();
        istream.close();
        ostream.close();
    }
}