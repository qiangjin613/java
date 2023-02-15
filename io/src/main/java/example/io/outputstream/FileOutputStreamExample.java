package example.io.outputstream;

import example.io.inputstream.FileInputStreamExample;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * java.io.FileOutputStream 示例：使用 write 读取文件数据。
 *
 * @see     FileInputStreamExample
 * @author  qiangj
 */
public class FileOutputStreamExample {
    public static void main(String[] args) {
        writeFile();
    }

    /**
     * 写入一个字节（当文件不存在时自动创建）
     * <p>
     * NOTE: 针对不同的 FileOutputStream 对象，使用 write 写入数据时，注意是否要覆盖之前的文件选择不同的构造器方法。
     */
    public static void writeFile() {
        // 创建一个覆盖写文件的，文件输出流对象（当文件不存在时创建）
        try (FileOutputStream fileOutputStream = new FileOutputStream("D:\\a.txt")) {
            // 写入一个字节
            fileOutputStream.write('a');
            // 写入字节数组
            fileOutputStream.write("Hello,World!".getBytes());
            // 写入字节数组 & 指定偏移量
            fileOutputStream.write("Hahahaha ~".getBytes(), 0, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建一个追加写文件的，文件输出流对象（当文件不存在时创建）
        try (FileOutputStream fileOutputStream = new FileOutputStream("D:\\a.txt", true)) {
            fileOutputStream.write("\nappend: Bilibili".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
