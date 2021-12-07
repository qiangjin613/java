package 口令加密算法;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/*
借助 BouncyCastle 实现 PBE
 */
public class PBETest {
    public static void main(String[] args) throws GeneralSecurityException {
        /* 注册BouncyCastle */
        Security.addProvider(new BouncyCastleProvider());
        /* 明文 */
        String msg = "Hello,World!";
        /* 加密口令 */
        String pwd = "123abcd";
        /* 16 byte 随机盐 */
        byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);
        System.out.printf("salt: %032x\n", new BigInteger(1, salt));

        // 加密
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encrypt(pwd, salt, data);
        System.out.println("encrypted: " + Base64.getEncoder().encodeToString(encrypted));

        // 解密
        byte[] decrypted = decrypt(pwd, salt, encrypted);
        System.out.println("decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }

    /**
     * 加密
     * @param pwd 密钥
     * @param salt 盐
     * @param input 明文
     * @return 密文
     */
    public static byte[] encrypt(String pwd, byte[] salt, byte[] input) throws GeneralSecurityException {
        PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
        /* 指定使用的算法 */
        SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
        SecretKey skey = skeyFactory.generateSecret(keySpec);
        /* 循环次数越多，暴力破解需要的计算量就越大 */
        PBEParameterSpec pbeps = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
        cipher.init(Cipher.ENCRYPT_MODE, skey, pbeps);
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(String pwd, byte[] salt, byte[] input) throws GeneralSecurityException {
        PBEKeySpec keySpec = new PBEKeySpec(pwd.toCharArray());
        SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
        SecretKey skey = skeyFactory.generateSecret(keySpec);
        PBEParameterSpec pbeps = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
        cipher.init(Cipher.DECRYPT_MODE, skey, pbeps);
        return cipher.doFinal(input);
    }
}
/*
（用户输入的口令长度不是固定的）
如果我们把salt和循环次数固定，就得到了一个通用的“口令”加密软件。
如果我们把随机生成的salt存储在U盘，就得到了一个“口令” + USB Key的加密软件，它的好处在于，即使用户使用了一个非常弱的口令，没有USB Key仍然无法解密，因为USB Key存储的随机数密钥安全性非常高。

Notice:
1. PBE算法通过用户口令和安全的随机salt计算出Key，然后再进行加密；
2. PBE算法内部使用的仍然是标准对称加密算法（例如AES）。
 */
