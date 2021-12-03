package 哈希算法;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
使用
 */
public class MD5Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 创建一个 MessageDigest 实例
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 反复调用 update 输入数据
        md5.update("Hello".getBytes(StandardCharsets.UTF_8));
        md5.update("你好！".getBytes(StandardCharsets.UTF_8));
        /* 获得 byte[] 数组表示的摘要 */
        byte[] digest = md5.digest();
        System.out.println(Arrays.toString(digest));
        /* 转换为十六进制的字符串 */
        System.out.println(new BigInteger(1, digest).toString(16));
    }
}
