package 非对称加密算法;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;

/*
RSA 非对称加密算法
Notice:
1. Cipher 的加密解密有最大长度限制：加密长度不超过 117 Byte，解密长度不超过 128 Byte。（这是个BUG，待解决...）
 */
public class RSATest {
    public static void main(String[] args) throws Exception {
        /* 明文 */
        byte[] plain = "Hello,world!".getBytes(StandardCharsets.UTF_8);
        /* 创建两个角色 */
        Person2 a = new Person2("A");
        /* 输出 公钥-私钥 对 */
        byte[] pk = a.getPublicKey();
        byte[] sk = a.getPrivateKey();
        System.out.println(String.format("public key: %x", new BigInteger(1, pk)));
        System.out.println(String.format("private key: %x", new BigInteger(1, sk)));
        // 使用 A 的公钥加密明文
        byte[] encrypted = a.encrypt(plain);
        System.out.println(String.format("encrypted: %x", new BigInteger(1, encrypted)));
        // 使用私钥解密密文
        byte[] decrypted = a.decrypt(encrypted);
        System.out.println("decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }
}

class Person2 {
    public String name;
    private PrivateKey sk;
    private PublicKey pk;

    public Person2(String name) throws NoSuchAlgorithmException {
        this.name = name;
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
        kpGen.initialize(1024);
        KeyPair kp = kpGen.generateKeyPair();
        sk = kp.getPrivate();
        pk = kp.getPublic();
    }
    /* 将 私钥/公钥 导出为字节数组 */
    public byte[] getPrivateKey() {
        return sk.getEncoded();
    }
    public byte[] getPublicKey() {
        return pk.getEncoded();
    }

    /**
     * 使用公钥加密
     * @param msg 明文
     * @return 密文
     */
    public byte[] encrypt(byte[] msg) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        return cipher.doFinal(msg);
    }

    /**
     * 使用私钥解密
     * @param input 密文
     * @return 明文
     */
    public byte[] decrypt(byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, sk);
        return cipher.doFinal(input);
    }
}
