package 编码算法;

/*
使用 java.util 包下的 Base64 进行编码和解码操作。
 */

import java.util.Arrays;
import java.util.Base64;

public class Base64Test {
    public static void main(String[] args) {
        /* 在 Java 中，二进制数据就是 byte[] 数组 */
        byte[] input = new byte[] { 1, 2, 3, 4, 5, 6 };

        // 编码
        String encoded = Base64.getEncoder().encodeToString(input);
        System.out.println(encoded);

        // 解码
        byte[] decoded = Base64.getDecoder().decode(encoded);
        System.out.println(Arrays.toString(decoded));
    }
}

/*
如果输入的 byte[] 数组长度不是 3 的整数倍怎么办？
这种情况下，就需要对输入的数据末尾补充一个或两个 0x00。
编码后，在结尾加一个=表示补充了1个0x00，加两个=表示补充了2个0x00。
解码的时候，去掉末尾补充的一个或两个0x00即可。

默认情况下，编码结果字符序列长度为 4 的倍数（编码前补充了 0x00，编码后自然多了 =）。
可以用 withoutPadding() 去掉填充的 =。
注：以上两种情况对解码不会有任何影响，因为编码后的长度加上 = 总是 4 的倍数，所以即使不加 = 也可以计算出原始输入的 byte[]。
 */

class Base64Test2 {
    public static void main(String[] args) {
        byte[] input = new byte[] { 1, 2, 3, 4 };
        /* 编码1 */
        String encoded = Base64.getEncoder().encodeToString(input);
        /* 编码2 */
        String encoded2 = Base64.getEncoder().withoutPadding().encodeToString(input);
        System.out.println(encoded + "\n" + encoded2);

        byte[] decoded = Base64.getDecoder().decode(encoded);
        byte[] decoded2 = Base64.getDecoder().decode(encoded2);
        System.out.println(Arrays.toString(decoded) + "\n" + Arrays.toString(decoded2));
    }
}

/*
因为标准的Base64编码会出现+、/和=，所以不适合把Base64编码后的字符串放到URL中。
一种针对URL的Base64编码可以在URL中使用的Base64编码，它仅仅是把+变成-，/变成_
 */
class Base64Test3 {
    public static void main(String[] args) {
        byte[] input = new byte[] { 1, 2, 127, 0 };

        String encoded = Base64.getEncoder().encodeToString(input);
        System.out.println(encoded);
        String encoded2 = Base64.getUrlEncoder().encodeToString(input);
        System.out.println(encoded2);

        byte[] decoded = Base64.getUrlDecoder().decode(encoded2);
        System.out.println(Arrays.toString(decoded));
    }
}

