package 对称加密算法;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 使用 AES 的 ECB 模式加密|解密。
 * Notice:
 * 1. ECB 模式必须要一个固定长度（128 bit）的密钥
 * 2. 固定的明文会生成固定的密文。
 */
public class AESTest {
    public static void main(String[] args) throws GeneralSecurityException {
        /* 原文 */
        String msg = "你好，生活！";
        /* 128 位（bit） 密钥 = 16 字节（byte） */
        byte[] key = "1212121212121212".getBytes(StandardCharsets.UTF_8);

        // 加密
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encrypt(key, data);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

        // 解密
        byte[] decrypted = decrypt(key, encrypted);
        System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }

    /**
     * AES 的 ECB 模式加密
     * @param key 密钥
     * @param input 原文
     * @return 密文
     */
    public static byte[] encrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        // 1. 根据 算法名称/工作模式/填充模式 获取 Cipher 实例
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // 2. 根据 算法名称 初始化 SecretKeySpec 实例，并指明特定长度的密钥
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        // 3. 使用 SecretKeySpec 实例初始化 Cipher 实例，并设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        // 4. 传入明文，获得密文
        return cipher.doFinal(input);
    }

    /**
     * AES 的 ECB 模式解密
     * @param key 密钥
     * @param input 密文
     * @return 原文
     */
    public static byte[] decrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        /* 置为解密模式 */
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(input);
    }
}

/**
 * 使用 AES 的 CBC 模式加密|解密。
 * Notice:
 * 1. 需要一个随机数作为 IV 参数
 * 2. 每次生成的密文都不同
 */
class AESTest2 {
    public static void main(String[] args) throws GeneralSecurityException {
        /* 原文 */
        String msg = "你好，生活！";
        /* 256 位（bit） 密钥 = 32 字节（byte） */
        byte[] key = "1234567890abcdef1234567890abcdef".getBytes(StandardCharsets.UTF_8);

        // 加密
        byte[] data = msg.getBytes(StandardCharsets.UTF_8);
        byte[] encrypted = encrypt(key, data);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(encrypted));

        // 解密
        byte[] decrypted = decrypt(key, encrypted);
        System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }

    /**
     * AES 的 CBC 模式加密
     * @param key 密钥
     * @param input 原文
     * @return 密文
     */
    public static byte[] encrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        // 1. 根据 算法名称/工作模式/填充模式 获取 Cipher 实例
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 2. 根据 算法名称 初始化 SecretKeySpec 实例，并指明特定长度的密钥
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        // 3. 生成一个 16 byte 的初始化向量
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] iv = sr.generateSeed(16);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        // 4. 使用 SecretKeySpec 实例初始化 Cipher 实例，并设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivps);
        // 5. 传入明文，获得密文
        byte[] data = cipher.doFinal(input);
        // 6. 将 iv 和密文一起返回
        return join(iv, data);
    }

    /**
     * AES 的 CBC 模式解密
     * @param key 密钥
     * @param input 密文
     * @return 原文
     */
    public static byte[] decrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        // 把 input 分割成 IV 和密文
        byte[] iv = new byte[16];
        byte[] data = new byte[input.length - 16];
        System.arraycopy(input, 0, iv, 0, 16);
        System.arraycopy(input, 16, data, 0, data.length);

        // 解密操作
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivps = new IvParameterSpec(iv);
        /* 置为解密模式 */
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivps);
        return cipher.doFinal(data);
    }

    public static byte[] join(byte[] bs1, byte[] bs2) {
        byte[] r = new byte[bs1.length + bs2.length];
        System.arraycopy(bs1, 0, r, 0, bs1.length);
        System.arraycopy(bs2, 0, r, bs1.length, bs2.length);
        return r;
    }
}
/*
Notice:
在运行过程中，程序可能抛出 java.security.InvalidKeyException: Illegal key size 异常。
是因为 JRE 中自带的“local_policy.jar ”和“US_export_policy.jar”是支持 128 位密钥的加密算法，
而当我们要使用 256 位密钥算法的时候，已经超出它的范围，无法支持。
可以去官方下载JCE无限制权限策略文件（替换掉原有的文件）。
JDK 8 的下载地址：https://www.oracle.com/java/technologies/javase-jce8-downloads.html
 */
