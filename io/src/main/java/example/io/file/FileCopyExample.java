package example.io.file;

import example.io.inputstream.FileInputStreamExample;
import example.io.outputstream.FileOutputStreamExample;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件输入/输出流综合应用：文件拷贝
 *
 * @see     FileOutputStreamExample
 * @see     FileInputStreamExample
 * @author  qiangj
 */
public class FileCopyExample {
    public static void main(String[] args) {
        fileCopy();
        fileCopy2();
        fileCopy3();
    }

    /**
     * 方式 1：使用文件输入输出流 copy 文件
     * <ol>
     * <li> 创建文件输入流读取文件
     * <li> 创建文件输出流，将读取的文件数据写入指定的文件
     * </ol>
     * NOTE：在写入文件时，如果使用了缓冲区，再写入数据时，要写入真实长度的字节，避免写入过多的文件。
     * 如：使用 write 写入一个 byte[]，在没有指定写入偏移量的情况下，可能会写入上一次缓冲区的数据，造成文件损坏！
     */
    private static void fileCopy() {
        String fileSourcePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\file\\FileCopyExample.java";
        String fileTargetPath = "D:\\FileCopyExample.java.txt";
        try (FileInputStream fileInputStream = new FileInputStream(fileSourcePath);
                FileOutputStream fileOutputStream = new FileOutputStream(fileTargetPath)) {
            // 定义一个 1024 byte 的缓冲区
            byte[] buffer = new byte[1024];
            int readLength;
            // 边读边写
            while ((readLength = fileInputStream.read(buffer)) != -1) {
                // 指定偏移量写入
                fileOutputStream.write(buffer, 0, readLength);
                // 会造成文件数据过多的情况（如果是一个照片或者音频的话，就会出现文件损坏打不开的情况）：
                // fileOutputStream.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 方式 2：使用处理流 BufferedReader/BufferedWriter copy 文件
     * <p>
     * NOTE：Reader 和 Writer 是字符流，适合处理字符文本，如果操作二进制文件，可能会因为文件编码/解码的操作导致文件损坏。
     */
    private static void fileCopy2() {
        String fileSourcePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\file\\FileCopyExample.java";
        String fileTargetPath = "D:\\FileCopyExample2.java.txt";
        // 读取写二进制文件，造成的文件损坏
        // String fileSourcePath = "D:\\aaa.png";
        // String fileTargetPath = "D:\\aaa（3）.png";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileSourcePath));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileTargetPath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 方式 3：使用 java.io.BufferedInputStream 和 java.io.BufferedOutputStream copy 文件
     * <p>
     * Think：可以使用字节流操作文本文件吗？    |-->  当然ok
     */
    private static void fileCopy3() {
        String fileSourcePath = "D:\\aaa.png";
        String fileTargetPath = "D:\\aaa（2）.png";
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(Paths.get(fileSourcePath)));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(fileTargetPath)))) {
            // 这里使用字节数组做一个缓冲
            byte[] byteDataArray = new byte[1024];
            int readLength;
            while ((readLength = bufferedInputStream.read(byteDataArray)) != -1) {
                bufferedOutputStream.write(byteDataArray, 0, readLength);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
