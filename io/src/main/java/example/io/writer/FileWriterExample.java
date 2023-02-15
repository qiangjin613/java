package example.io.writer;

import java.io.FileWriter;
import java.io.IOException;

/**
 * java.io.FileWriter 使用示例
 * <p>
 * NOTE：使用 java.io.OutputStreamWriter.write(int) 一定要关闭流或者 flush，才能将数据真正写入到文件中。
 * 因为在关闭或者 flush 时，会调用 java.io.OutputStream#write(byte[], int, int) 写入数据。
 */
public class FileWriterExample {
    public static void main(String[] args) {
        writeFile();
        writeFile2();
    }

    /**
     * 使用 java.io.OutputStreamWriter#write() 写入文件
     */
    private static void writeFile() {
        String filePath = "D:\\a.txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            // 写入 char
            fileWriter.write('你');
            // 写入 char[]
            fileWriter.write("好呀~".toCharArray());
            // 指定偏移量写入 char[]
            fileWriter.write("Hahahahahaha".toCharArray(), 0, 6);
            // 写入 string
            fileWriter.write("Hello");
            // 写入指定偏移量的 string
            fileWriter.write("北上广深杭，雨过天晴", 0, 3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 使用 java.io.OutputStreamWriter.write(int) 一定要关闭流或者 flush
            try {
                if (fileWriter != null)
                    fileWriter.close(); // close 等价于 flush + close 操作
                // or
                // fileWriter.flush(); fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 对 writeFile() 的优化：可避免漏掉编写 flush、close 操作带来的影响
     * <p>
     * Q：为什么 '你' 无法写入呢？？？也没有出现乱码
     */
    private static void writeFile2() {
        String filePath = "D:\\b.txt";
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            System.out.println((int) '你');
            // 写入 char
            fileWriter.write('你');
            // 写入 char[]
            fileWriter.write("好呀~".toCharArray());
            // 指定偏移量写入 char[]
            fileWriter.write("Hahahahahaha".toCharArray(), 0, 6);
            // 写入 string
            fileWriter.write("Hello");
            // 写入指定偏移量的 string
            fileWriter.write("北上广深杭，雨过天晴", 0, 3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
