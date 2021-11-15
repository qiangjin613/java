import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
【目录】
Files 工具类包含大部分我们需要的目录操作和文件操作方法。
出于某种原因，它们没有包含删除目录树相关的方法，因此在下面实现该方法。
 */

/**
 * 删除目录树
 */
class RmDir {
    public static void rmdir(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
/*
删除目录树的方法实现依赖于 Files.walkFileTree()，"walking" 目录树意味着遍历每个子目录和文件。
Visitor 设计模式提供了一种标准机制来访问集合中的每个对象，然后你需要提供在每个对象上执行的操作。
此操作的定义取决于实现的 FileVisitor 的四个抽象方法，包括：
1. preVisitDirectory()：在访问目录中条目之前在目录上运行。
2. visitFile()：运行目录中的每一个文件。
3. visitFileFailed()：调用无法访问的文件。
4. postVisitDirectory()：在访问目录中条目之后在目录上运行，包括所有的子目录。

为了简化，java.nio.file.SimpleFileVisitor 提供了所有方法的默认实现。
这样，在我们的匿名内部类中，我们只需要重写非标准行为的方法：
    visitFile() 和 postVisitDirectory() 实现删除文件和删除目录。
两者都应该返回标志位决定是否继续访问(这样就可以继续访问，直到找到所需要的)。
 */


/*
作为探索目录操作的一部分，现在我们可以有条件地删除已存在的目录。
在以下例子中，makeVariant() 接受基本目录测试，并通过旋转部件列表生成不同的子目录路径。
这些旋转与路径分隔符 sep 使用 String.join() 贴在一起，然后返回一个 Path 对象。
 */
class Directories {
    static Path test = Paths.get("test");
    static String sep = FileSystems.getDefault().getSeparator(); /* 获取当前操作系统的路径分隔符 */
    static List<String> parts = Arrays.asList("foo", "bar", "baz", "bag");

    static Path makeVariant() {
        Collections.rotate(parts, 1);
        return Paths.get("test", String.join(sep, parts));
    }

    // refreshTestDir() 用于检测 test 目录是否已经存在。若存在，则使用我们新工具类 rmdir() 删除其整个目录：
    static void refreshTestDir() throws IOException {
        if (Files.exists(test))
            RmDir.rmdir(test);
        if (!Files.exists(test))
            Files.createDirectory(test);
    }

    public static void main(String[] args) throws IOException {
        refreshTestDir();
        Path path = test.resolve("Hello.txt"); /* resolve() 将文件名添加到 test Path 的末尾 */
        Files.createFile(path); /* createFile() 使用参数 Path 创建一个空文件 */
        Path variant = makeVariant();
        try {
            Files.createDirectory(variant);
            /* 注释：
            1）使用 createDirectory() 来创建多级路径，但是这样会抛出异常，因为这个方法只能创建单级路径
            2）对于已经存在的目录调用 createDirectory() 将会抛出异常
            */
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Nope, that doesn't work.");
        }
        populateTestDir();
        Path tempdir = Files.createTempDirectory(test, "DIR_");
        Files.createTempFile(tempdir, "pre", ".non");

        // 使用 2 种方式遍历某个路径：
        Files.newDirectoryStream(test).forEach(System.out::println);
        System.out.println("*************");
        Files.walk(test).forEach(System.out::println);
    }

    static void populateTestDir() throws IOException {
        for (int i = 0; i < parts.size(); i++) {
            Path variant = makeVariant();
            if (!Files.exists(variant)) {
                Files.createDirectories(variant); /* 使用 createDirectories() 创建完整的目录路径 */
                Files.copy(Paths.get("chapter_17_文件\\part_02_目录.java"), variant.resolve("File.txt"));
                Files.createTempFile(variant, null, null); /* 使用 createTempFile() 生成一个临时文件，如果未指定后缀，它将默认使用".tmp"作为后缀 */
            }
        }
    }
}
/*
【newDirectoryStream() VS walk()】
为了展示结果，我们首次使用看起来很有希望的 newDirectoryStream()，
但事实证明这个方法只是返回 test 目录内容的 Stream 流，并没有更多的内容。
要获取目录树的全部内容的流，请使用 Files.walk()。
 */
