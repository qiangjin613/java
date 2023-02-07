package example.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * java.io.BufferedWriter 示例
 */
public class BufferedWriterExample {
    public static void main(String[] args) throws IOException {
        write();
    }

    /**
     * 向文件中写入数据
     * <p>
     * NOTE：
     * <ul>
     * <li> 写入文件的方式（追加 or 覆盖）依旧在节点流中控制；
     * <li> 可使用 java.io.BufferedWriter#newLine() 便捷的向文件中插入一个与系统相关的换行符
     * </ul>
     */
    private static void write() throws IOException {
        String filePath = "D:\\a.txt";
        // 以追加的方式写入文件内容
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
        bufferedWriter.write("hello, Java I/O!");
        // 插入一个与系统相关的换行符
        bufferedWriter.newLine();
        bufferedWriter.write("hello, Java I/O!");
        bufferedWriter.write("hello, Java I/O!");

        bufferedWriter.close();
    }
}
