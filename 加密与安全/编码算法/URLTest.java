package 编码算法;

/*
使用 java.net 包的 URLEncoder、URLDecoder 来对任意字符串进行 URL 编码、解码。
 */

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLTest {
    public static void main(String[] args) {
        String encoded = null;
        try {
            // 编码
            encoded = URLEncoder.encode("中 文 !", StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("编码后：" + encoded);

        String decoded = null;
        try {
            // 解码
            decoded = URLDecoder.decode(encoded, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("解码后：" + decoded);
    }
}
/*
示例解析：
中 -> %E4%B8%AD
文 -> %E6%96%87
! -> %21

Notice:
1. 虽然 ! 是 ASCII 中包含的字符，但在 URL 编码中也要对其进行编码。
2. 和标准的URL编码稍有不同，URLEncoder 把空格字符编码成 +，而现在的 URL 编码标准要求空格被编码为 %20，
    不过，服务器都可以处理这两种情况。
 */
