package 第三方JAR包;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/*

 */
public class BouncyCastleTest {
    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        System.out.println(System.getProperty("java.class.path"));
    }
}
/*
CLASSPATH 路径是 Java 虚拟机（JVM）调用 .class 文件时寻找的路径.
C:\Users\administer\Desktop\bcprov-jdk15on-170.jar
 */
