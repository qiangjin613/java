package example;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * System.in/out "标准"输入/输出流示例：
 *
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <caption>标准输入/输出流</caption>
 *  <tr>
 *    <td></td>
 *    <td ALIGN=CENTER><em>类型</em></td>
 *    <td ALIGN=CENTER><em>默认设备</em></td>
 *  </tr>
 *  <tr>
 *    <td><b>System.in</b></td>
 *    <td>标准输入</td>
 *    <td>键盘</td>
 *  </tr>
 *  <tr>
 *    <td><b>System.out</b></td>
 *    <td>标准输出</td>
 *    <td>控制台</td>
 *  </tr>
 * </table>
 *
 * @author qiangj
 */
public class StandardInputOutputExample {
    public static void main(String[] args) {
        /*
        标准输入 System.in 的编译类型为 java.io.InputStream
        运行类型为 class java.io.BufferedInputStream
         */
        InputStream inputStream = System.in;
        System.out.println(inputStream.getClass());

        /*
        标准输出 System.out 的编译类型为：java.io.PrintStream
        运行类型为 class java.io.PrintStream
         */
        PrintStream printStream = System.out;
        System.out.println(printStream.getClass());

        // 扫描器 Scanner 就是从输入流 BufferedInputStream 中取数据
        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.next());
    }
}
