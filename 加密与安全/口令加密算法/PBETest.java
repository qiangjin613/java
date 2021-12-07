package 口令加密算法;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

/*
借助 BouncyCastle 实现 PBE
 */
public class PBETest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        /* 注册BouncyCastle */
        Security.addProvider(new BouncyCastleProvider());
        /* 明文 */
        String msg = "Hello,World!";
        /* 加密口令 */
        String pwd = "123abc";
        /* 16 byte 随机盐 */
        byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);
        System.out.printf("salt: %032x\n", new BigInteger(1, salt));


    }

    public static byte[] encrypt(String pwd, byte[] salt, byte[] input) {

    }
}
