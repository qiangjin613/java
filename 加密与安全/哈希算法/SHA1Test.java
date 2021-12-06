package 哈希算法;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
使用 java.security 包下的 MessageDigest 类进行 SHA-1 加密操作。

SHA-1是由美国国家安全局开发的，SHA算法实际上是一个系列，包括:
SHA-0（已废弃）、SHA-1、SHA-256、SHA-512等。

在Java 中使用 SHA-1 和 MD5 一样，只需把算法名改为 “SHA-1” 即可。
 */
public class SHA1Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 1. 创建一个 MessageDigest 实例
        MessageDigest md5 = MessageDigest.getInstance("SHA-1");
        // 2. 反复调用 update 输入数据
        md5.update("Hello".getBytes(StandardCharsets.UTF_8));
        md5.update("World".getBytes(StandardCharsets.UTF_8));
        // 3. 获得 byte[] 数组表示的摘要（其 len == 20）
        byte[] digest = md5.digest(); /* 生成 “HelloWorld” 的 MD5 摘要 */
        System.out.println(Arrays.toString(digest));
        /* 转换为十六进制的字符串 */
        System.out.println(new BigInteger(1, digest).toString(16));
    }
}
