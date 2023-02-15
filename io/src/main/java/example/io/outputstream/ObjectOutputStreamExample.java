package example.io.outputstream;

import example.io.inputstream.ObjectInputStreamExample;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * java.io.ObjectOutputStream 示例：将对象序列化后保存到文件中
 *
 * @see ObjectInputStreamExample
 */
public class ObjectOutputStreamExample implements Serializable {

    private static final long serialVersionUID = 2125959371742181058L;

    public static void main(String[] args) {
        writeObject();
    }

    /**
     * 将对象序列化写入到文件中
     */
    private static void writeObject() {
        String filePath = "D:\\TempSerializableObject.txt";
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.write(5);
            objectOutputStream.write("Hello".getBytes(StandardCharsets.UTF_8));
            objectOutputStream.writeObject(new ObjectOutputStreamExample());
            objectOutputStream.writeUTF("大哥");
            objectOutputStream.writeInt(1);
            objectOutputStream.writeBoolean(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
