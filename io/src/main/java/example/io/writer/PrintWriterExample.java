package example.io.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * java.io.PrintWriter 示例
 * <p>
 * NOTE：不管输出到哪里，只有调用了 close() 才会将数据写入进去。
 *
 * @author qiangj
 */
public class PrintWriterExample {
    public static void main(String[] args) throws IOException {
        // 使用标准输出：到显示器
        PrintWriter printWriter = new PrintWriter(System.out);
        printWriter.print("大哥，你好！");
        printWriter.close();
        // 输出到文件
        PrintWriter printWriter2 = new PrintWriter(new FileWriter("D:\\a.txt"));
        printWriter2.print("大哥，你好！");
        printWriter2.close();
    }
}
