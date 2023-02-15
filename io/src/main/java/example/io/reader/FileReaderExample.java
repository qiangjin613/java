package example.io.reader;

import java.io.FileReader;
import java.io.IOException;

/**
 * java.io.FileReader 使用示例
 */
public class FileReaderExample {
    public static void main(String[] args) {
        readFile();
        readFileBatch();
    }

    /**
     * 使用 java.io.InputStreamReader#read() 一个字符一个字符的读取文件
     */
    private static void readFile() {
        String filePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\reader\\FileReaderExample.java";
        try (FileReader fileReader = new FileReader(filePath)) {
            int data;
            while ((data = fileReader.read()) != -1) {
                System.out.print((char) data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 java.io.Reader#read(char[]) 批量读取文件
     */
    private static void readFileBatch() {
        String filePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\reader\\FileReaderExample.java";
        try (FileReader fileReader = new FileReader(filePath)) {
            int dataLength;
            // 相当于是个缓冲区
            char[] charArray = new char[8];
            while ((dataLength = fileReader.read(charArray)) != -1) {
                System.out.print(new String(charArray, 0, dataLength));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
