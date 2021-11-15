import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/*
【文件读写】
如果一个文件很“小”，也就是说“它运行得足够快且占用内存小”，
那么 java.nio.file.Files 类中的实用程序将帮助你轻松读写文本和二进制文件。
 */

/**
 * 通过 Files.readAllLines() 一次读取整个小文件
 */
class ListOfLines {
    public static void main(String[] args) throws IOException {
        /* 跳过注释行，其余的内容每行只打印一半 */
        Files.readAllLines(Paths.get("chapter_17_文件\\章节概括.md")).stream()
                .filter(line -> !line.startsWith("#"))
                .map(line -> line.substring(0, line.length()/2))
                .forEach(System.out::println);
    }
}
/*
readAllLines() 还有一个重载版本，包含一个 Charset 参数来存储文件的 Unicode 编码。
 */


/**
 * Files.write() 被重载以写入 byte 数组或任何 Iterable 对象（它也有 Charset 选项）
 */
class Writing {
    static Random rand = new Random(47);
    static final int SIZE = 1000;

    public static void main(String[] args) throws IOException {
        byte[] bytes = new byte[SIZE];
        /* 使用 Random 来创建一个随机的 byte 数组 */
        rand.nextBytes(bytes);

        // 创建将要使用的文件
        Directories.refreshTestDir();
        Path path = Paths.get("test/test.txt");
        if (Files.exists(path)) {
            Files.delete(path);
        }
        Files.createFile(path);

        Files.write(path, bytes);
        System.out.println("test/test.txt: " + Files.size(path));

        // 这里抛出异常：java.nio.charset.MalformedInputException: Input length = 1
        // 揣测原因可能是由于上述文件中，解析字符序列失败导致
        List<String> lines = Files.readAllLines(path);
        Path path2 = Paths.get("test/test2.txt");
        Files.write(path2, lines);
        System.out.println("test/test2.txt: " + Files.size(path2));
    }
}

/*
如果文件大小有问题怎么办？ 比如说：
1. 文件太大，如果一次性读完整个文件，你可能会耗尽内存。
2. 只需要在文件的中途工作以获得所需的结果，因此读取整个文件会浪费时间。

解决方法：将文件内容以流的形式读取。
 */

/**
 * 使用 Files.lines() 方便地将文件转换为行的 Stream
 */
class ReadLineStream {
    public static void main(String[] args) throws IOException {
        Files.lines(Paths.get("chapter_17_文件\\章节概括.md"))
                .skip(6)
                .findFirst()
                .ifPresent(System.out::println);
    }
}

/**
 * Files.lines() 对于把文件处理行的传入流时非常有用，
 * 但是如果你想在 Stream 中读取，处理或写入怎么办？
 * 这就需要稍微复杂的代码：
 */
class StreamInAndOut {
    public static void main(String[] args) throws IOException {
        Directories.refreshTestDir();
        try (
            Stream<String> input = Files.lines(Paths.get("chapter_17_文件\\part_06_文件读写.java"));
            PrintWriter output = new PrintWriter("test/part_06_文件读写.txt")
        ) {
            input.map(String::toUpperCase).forEach(output::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
