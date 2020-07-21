import sun.misc.BASE64Encoder;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * 图片转换Base64的方法
 *
 * @author Liu Yuyang.
 * @version 1.0.
 */
public class ImageToBase64 {
    public ImageToBase64() {
    }

    /**
     * 网络图片转换Base64的方法
     *
     * @param netImagePath net image path 
     */
    public static String NetImageToBase64(String netImagePath,String type) {
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        String strNetImageToBase64="";
        try {
            // 创建URL
            URL url = new URL(netImagePath);
            final byte[] by = new byte[1024];
            // 创建链接
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            strNetImageToBase64 = encoder.encode(data.toByteArray());
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "data:image/"+type +";base64,"+strNetImageToBase64;
    }


    /**
     * 本地图片转换Base64的方法
     *
     * @param imgPath image path 
     */

    public static String ImageToBase64(String imgPath,String type) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return "data:image/"+type+";base64,"+ encoder.encode(Objects.requireNonNull(data));
    }
}