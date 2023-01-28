package example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件输入/输出流综合应用：文件拷贝
 *
 * @see     example.outputstream.FileOutputStreamExample
 * @see     example.inputstream.FileInputStreamExample
 * @author  qiangj
 */
public class FileCopyExample {
    public static void main(String[] args) {
        fileCopy();
    }

    /**
     * 思路 1
     * <ol>
     * <li> 创建文件输入流读取文件
     * <li> 创建文件输出流，将读取的文件数据写入指定的文件
     * </ol>
     * NOTE：在写入文件时，如果使用了缓冲区，再写入数据时，写入真实长度的字节，避免写入过多的文件。
     * 如：使用 write 写入一个 byte[]，在没有指定写入偏移量的情况下，可能会写入上一次缓冲区的数据，造成文件损坏！
     */
    public static void fileCopy() {
        String fileSourcePath = "D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\FileCopyExample.java";
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
}
