package example.io.inputstream;

import example.io.outputstream.FileOutputStreamExample;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * java.io.FileInputStream 示例：使用 read 读取文件数据。
 *
 * @see     FileOutputStreamExample
 * @author  qiangj
 */
public class FileInputStreamExample {
    public static void main(String[] args) {
        readFile();
        readFile2();
    }

    /**
     * 使用 read() 单个字节读取文件数据。
     * <p>
     * NOTE：
     * <ul>
     * <li> 读取多字节的数据时，会出现乱码，比如中文
     * <li> 因为是单个字节读取，所以效率比较低。（可使用 read(byte[] b) 读取，提高效率）
     * </ul>
     */
    public static void readFile() {
        try (FileInputStream fileInputStream = new FileInputStream("D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\inputstream\\FileInputStreamExample.java")) {
            // 使用读取文件数据
            int byteData;
            while ((byteData = fileInputStream.read()) != -1) {
                System.out.print((char) byteData);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 read(byte b[]) 读取多个字节数据（byte[] 相当于是一个 Buffer）
     * <p>
     * NOTE：
     * <ul>
     * <li> 读取多字节的数据时，也会出现乱码。
     *      即使使用了相同的字符集（如文件的读写都是 UTF-8），也会因缓冲区 byte[] 的长度问题，造成部分的数据乱码
     *      （如：3 个中文是 9 个字符，从字节流中读取 8 个字符进行解码，必然会造成数据的乱码）。
     *      彻底解决乱码问题，使用文件字符流 FileReader/FileWriter 进行操作
     * </ul>
     */
    public static void readFile2() {
        try (FileInputStream fileInputStream = new FileInputStream("D:\\CodeRepositories\\java\\io\\src\\main\\java\\example\\inputstream\\FileInputStreamExample.java")) {
            // 使用读取文件数据
            int byteDataLength;
            // 一次读取 8 个字节
            byte[] byteBufferArray = new byte[8];
            while ((byteDataLength = fileInputStream.read(byteBufferArray)) != -1) {
                // 为了避免上此读取数据造成的遗留问题，这里对 byteBufferArray 指定字节的顺序进行读取
                System.out.print(new String(byteBufferArray, 0, byteDataLength));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
