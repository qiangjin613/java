package example.io.reader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * java.io.InputStreamReader 示例：将字节流转换为字符流，解决字节流的乱码问题
 */
public class InputStreamReaderExample {
    public static void main(String[] args) {
        String filePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\reader\\InputStreamReaderExample.java";
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {
            String readStr;
            while ((readStr = bufferedReader.readLine()) != null) {
                System.out.println(readStr);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
