package example.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 使用 java.io.BufferedReader 读取文本文件
 * <p>
 * NOTE：关闭处理流时，只需关闭最外层的即可。
 */
public class BufferedReaderExample {
    public static void main(String[] args) throws IOException {
        read();
    }

    /**
     *
     */
    private static void read() throws IOException {
        String filePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\reader\\BufferedReaderExample.java";
        // 创建包装流 BufferedReader
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        // 按行读取文件
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        // 关闭流：只需要关闭最外层的处理流即可，应为底层他会自动的关闭节点流
        bufferedReader.close();
    }
}
