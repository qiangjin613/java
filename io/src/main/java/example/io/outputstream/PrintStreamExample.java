package example.io.outputstream;

import java.io.IOException;
import java.io.PrintStream;

/**
 * 示例
 */
public class PrintStreamExample {
    public static void main(String[] args) throws IOException {
        // 标准输出流的编译类型就是 PrintStream
        PrintStream printStream = System.out;
        // 默认情况下，其输出数据的位置是显示器，等效于 System.out.println()
        printStream.print("Hello");
        // 也等效于：
        printStream.write("Hello".getBytes());

        // 可以通过 java.lang.System.setOut 修改输出流的位置
        System.setOut(new PrintStream("D:\\a.txt"));
        System.out.print("Hello");
    }
}
