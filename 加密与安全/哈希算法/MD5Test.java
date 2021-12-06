package 哈希算法;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
使用 java.security 包下的 MessageDigest 类进行 MD5 加密操作。

Notice:
1. digest() 方法只能被调用一次。digest() 方法被调用后，MessageDigest 对象被重新设置成其初始状态。
2.
 */
public class MD5Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 1. 创建一个 MessageDigest 实例
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 2. 反复调用 update 输入数据
        md5.update("Hello".getBytes(StandardCharsets.UTF_8));
        md5.update("World".getBytes(StandardCharsets.UTF_8));
        // 3. 获得 byte[] 数组表示的摘要（其 len == 16）
        byte[] digest = md5.digest(); /* 生成 “HelloWorld” 的 MD5 摘要 */
        System.out.println(Arrays.toString(digest));
        /* 转换为十六进制的字符串 */
        System.out.println(new BigInteger(1, digest).toString(16));

        md5.update("你好！".getBytes(StandardCharsets.UTF_8));
        byte[] digest1 = md5.digest();
        System.out.println(Arrays.toString(digest1));
        System.out.println(new BigInteger(1, digest1).toString(16));
    }
}
/*
虽然使用 md5.digest() 生成的 MD5 摘要都是长度为 16 的 byte 数组，
但是在转换为 16 进制的字符串的过程中，可能会忽略掉前导 0，导致16为字符长度不同。
如：使用 "new BigInteger(1, digest).toString(16)" 的写法会吞掉 0开头的，0最终会被省略。使用 String.format() 可以解决这个问题
 */
