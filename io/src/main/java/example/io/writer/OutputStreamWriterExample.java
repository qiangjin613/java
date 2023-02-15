package example.io.writer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 示例：将字节流转换为字符流
 */
public class OutputStreamWriterExample {
    public static void main(String[] args) {
        String filePath = "D:\\Temp转换流示例.txt";
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get(filePath)), StandardCharsets.UTF_8)) {
            outputStreamWriter.write("hello, 大哥");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
