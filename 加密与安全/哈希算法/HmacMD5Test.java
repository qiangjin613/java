package 哈希算法;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
使用 javax.crypto 包下的 KeyGenerator、SecretKey、SecretKeySpec、Mac 类进行 HmacMD5 加密操作。
 */
public class HmacMD5Test {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        /* 通过明文生成 HmacMD5 密文 */
        // 1. 通过算法名称 HmacHD5 获取 KeyGenerator 实例
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
        // 2. 通过 KeyGenerator 对象获取创建 SecretKey 实例
        SecretKey key = keyGen.generateKey();
        /* 打印生成的随机 secret_key，其 len =  64 */
        byte[] skey = key.getEncoded(); /* 获取 key */
        System.out.println(Arrays.toString(skey) + " len = " + skey.length);
        System.out.println(new BigInteger(1, skey).toString(16));
        // 3. 通过算法名称 HmacMD5 获取 Mac 实例
        Mac mac = Mac.getInstance("HmacMD5");
        // 4. 使用 SecretKey 初始化 Mac 实例
        mac.init(key);
        // 5. 对 Mac 实例反复调用 update 输入数据
        mac.update("Hello".getBytes(StandardCharsets.UTF_8));
        mac.update("Word".getBytes(StandardCharsets.UTF_8));
        // 6. 使用 Mac 实例的 doFinal() 方法获取最终的哈希值
        byte[] result = mac.doFinal();
        System.out.println(new BigInteger(1, result).toString(16) + "\n");

        /* 验证明文和密文 */
        // 1. 使用”认证码“和算法名称 HmacHD5 获取 SecretKeySpec 实例
        SecretKeySpec secretKey = new SecretKeySpec(skey, "HmacMD5");
        // 2. 获取 Mac 实例
        Mac mac2 = Mac.getInstance("HmacMD5");
        // 3. 使用生成的 SecretKeySpec 实例对 Mac 对象进行初始化
        mac2.init(secretKey);
        // 4. 使用 Mac 实例的 update() 输入明文数据
        mac2.update("HelloWord".getBytes(StandardCharsets.UTF_8));
        // 5. 通过 Mac 实例的 doFinal() 获得哈希值（该结果与 result 结果相同）
        byte[] result2 = mac2.doFinal();
        System.out.println(Arrays.toString(result2));
        System.out.println(new BigInteger(1, result2).toString(16));
    }
}
/*
由上例可得：存储密文时，也要存储“认证码”。
 */
