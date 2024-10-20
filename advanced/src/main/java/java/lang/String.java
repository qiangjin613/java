package java.lang;

/**
 * 自定义的 java.lang.String 类，用于展示 ClassLoader 的双亲委派机制
 */
public class String {

    static {
        System.out.println("loaded MyString");
    }

    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader());
    }
}
