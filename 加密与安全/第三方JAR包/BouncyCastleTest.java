package 第三方JAR包;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Set;

/*
使用第三方 BouncyCastleProvider 加密算法的第三方库和 MessageDigest 实现加密操作。
 */
public class BouncyCastleTest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 注册 BouncyCastle（注册只需要在启动时进行一次，后续就可以使用 BouncyCastle 提供的所有哈希算法和加密算法）
        Security.addProvider(new BouncyCastleProvider());
        // 根据以往套路使用
        MessageDigest md = MessageDigest.getInstance("RipeMD160");
        md.update("HelloWorld".getBytes(StandardCharsets.UTF_8));
        byte[] result = md.digest();
        System.out.println(new BigInteger(1, result).toString(16));

        // 通过以下方法，查看 Security 支持的所有 MessageDigest 算法
        Set<String> messageDigest = Security.getAlgorithms("MessageDigest");
        messageDigest.forEach(System.out::println);
    }
}
/*
需要将 BouncyCastle 的 jar 包引入项目中
 */
