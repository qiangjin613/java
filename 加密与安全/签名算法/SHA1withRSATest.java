package 签名算法;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;

/*
使用 SHA1withRSA 数字签名算法 Demo
 */
public class SHA1withRSATest {
    public static void main(String[] args) throws GeneralSecurityException {
        /* 生成 PSA 的 公钥-私钥 对 */
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
        kpGen.initialize(1024);
        KeyPair kp = kpGen.generateKeyPair();
        PrivateKey sk = kp.getPrivate();
        PublicKey pk = kp.getPublic();

        /* 明文 */
        byte[] msg = "Hello, I'm QJ".getBytes(StandardCharsets.UTF_8);

        /* 私钥签名（加密） */
        Signature s = Signature.getInstance("SHA1withRSA");
        s.initSign(sk);
        s.update(msg);
        byte[] signed = s.sign();
        System.out.println(String.format("signature: %x", new BigInteger(1, signed)));

        /* 公钥验签 */
        Signature v = Signature.getInstance("SHA1withRSA");
        v.initVerify(pk);
        v.update(msg);
        boolean valid = v.verify(signed);
        System.out.println("valid? " + valid);
    }
}
