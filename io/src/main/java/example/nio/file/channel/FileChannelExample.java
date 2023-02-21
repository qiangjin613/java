package example.nio.file.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例
 */
public class FileChannelExample {

    public static void main(String[] args) throws IOException {
        readFile();
    }

    /**
     * 一个简单的示例：读取文件到 Buffer 中
     */
    private static void readFile() throws IOException {
        // 创建 Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(47);

        // 通过 RandomAccessFile 获取一个对应的 FileChannel
        RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\a.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        // 将 Channel 中的数据读取到 Buffer 中，每次都会把 Buffer 读满
        int readLength = fileChannel.read(byteBuffer);
        while (readLength != -1) {
            System.out.println("读取了 " + readLength + " 字节");
            // 将 Buffer 反转，获取 Buffer 里面的内容
            byteBuffer.flip();
            while (byteBuffer.hasRemaining()) {
                System.out.println((char) byteBuffer.get());
            }
            // 在读取完 Buffer 中的内容后，清空 Buffer 的数据，准备进行下一次的读取
            byteBuffer.clear();
            readLength = fileChannel.read(byteBuffer);
        }

        randomAccessFile.close();
    }
}
