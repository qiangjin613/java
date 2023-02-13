package example.inputstream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

/**
 * java.io.ObjectInputStream 示例：从文件中读取数据，并反序列化
 *
 * @author  qiangj
 * @see     example.outputstream.ObjectOutputStreamExample
 */
public class ObjectInputStreamExample {
    public static void main(String[] args) {
        readObjectWithFile();
    }

    /**
     * 从序列化文件中读取数据
     * <p>
     * NOTE:
     * <ul>
     * <li> 读取文件（反序列化）的顺序与写入（序列化）的顺序要一致，否则将会出现 EOFException
     * <li> 在反序列化对象时，如果需要调用其运行时类型的方法，就需要将序列化对象的定义（class 文件）拷贝到可以引用的位置（包名也要一致）
     * </ul>
     */
    private static void readObjectWithFile() {
        String filePath = "D:\\TempSerializableObject.txt";
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            System.out.println(objectInputStream.read());
            byte[] byteArray = new byte[100];
            int readLength = objectInputStream.read(byteArray);
            System.out.println(new String(byteArray, 0, readLength, StandardCharsets.UTF_8));
            System.out.println(objectInputStream.readObject());
            System.out.println(objectInputStream.readUTF());
            System.out.println(objectInputStream.readInt());
            System.out.println(objectInputStream.readBoolean());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
