package 密钥交换算法;

import javax.crypto.KeyAgreement;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/*
DH 算法，交换密钥
 */
public class DHTest {
    public static void main(String[] args) {
        // 创建两个用户，并生成自己的公钥私钥对
        Person a = new Person("a");
        Person b = new Person("b");
        a.generteKeyPair();
        b.generteKeyPair();
        // 交互公钥，使用公钥生成密钥
        a.generateSecretKey(b.publicKey.getEncoded());
        b.generateSecretKey(a.publicKey.getEncoded());

        a.printKeys();
        b.printKeys();
    }
}
class Person {
    public final String name;
    public PublicKey publicKey;
    private PrivateKey privateKey;
    private byte[] secretKey;

    public Person(String name) {
        this.name = name;
    }

    /**
     * 生成本地 KeyPair，包含：publicKey、privateKey
     */
    public void generteKeyPair() {
        try {
            KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DH");
            kpGen.initialize(512);
            KeyPair kp = kpGen.generateKeyPair();
            privateKey = kp.getPrivate();
            publicKey = kp.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算密钥 secretKey
     * @param receivedPubKeyBytes 接受的对方的公钥
     */
    public void generateSecretKey(byte[] receivedPubKeyBytes) {
        try {
            // 从 byte[] 恢复 PublicKey
            X509EncodedKeySpec keySpe = new X509EncodedKeySpec(receivedPubKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("DH");
            PublicKey receivedPublicKey = kf.generatePublic(keySpe);
            // 生成本地密钥
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            /* 使用自己的 私钥 初始化 */
            keyAgreement.init(privateKey);
            /* 使用接受的对方的 公钥 */
            keyAgreement.doPhase(receivedPublicKey, true);
            // 生成 SecretKey 密钥
            secretKey = keyAgreement.generateSecret();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public void printKeys() {
        System.out.printf("Name: %s\n", name);
        System.out.printf("Private key: %x\n", new BigInteger(1, privateKey.getEncoded()));
        System.out.printf("Public key: %x\n", new BigInteger(1, publicKey.getEncoded()));
        System.out.printf("Secret key: %x\n", new BigInteger(1, secretKey));
    }
}
