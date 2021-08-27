import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
【目录】
Files 工具类包含大部分我们需要的目录操作和文件操作方法。
出于某种原因，它们没有包含删除目录树相关的方法，因此我们将实现并将其添加到 onjava 库中。
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
    static String sep = FileSystems.getDefault().getSeparator();
    static List<String> parts = Arrays.asList("foo", "bar", "baz", "bag");

    static Path makeVariant() {
        Collections.rotate(parts, 1);
        return Paths.get("test", String.join(sep, parts));
    }

    //
    static void refreshTestDir() throws IOException {
        if (Files.exists(test))
            RmDir.rmdir(test);
        if (!Files.exists(test))
            Files.createDirectory(test);
    }

    public static void main(String[] args) throws IOException {
        refreshTestDir();
        Files.createFile(test.resolve("Hello.txt"));
        Path variant = makeVariant();
        try {
            Files.createDirectory(variant);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Nope, that doesn't work.");
        }
        populateTestDir();
        Path tempdir = Files.createTempDirectory(test, "DIR_");
        Files.createTempFile(tempdir, "pre", ".non");
        Files.newDirectoryStream(test).forEach(System.out::println);
        System.out.println("*************");
        Files.walk(test).forEach(System.out::println);
    }

    static void populateTestDir() throws IOException {
        for (int i = 0; i < parts.size(); i++) {
            Path variant = makeVariant();
            if (!Files.exists(variant)) {
                Files.createDirectory(variant);
                Files.copy(Paths.get("chapter_17_文件\\part_02_目录.java"), variant.resolve("File.txt"));
                Files.createTempFile(variant, null, null);
            }
        }
    }
}
/*
refreshTestDir() 用于检测 test 目录是否已经存在。若存在，则使用我们新工具类 rmdir() 删除其整个目录。

检查是否 exists 是多余的，但我想说明一点，因为如果你对于已经存在的目录调用 createDirectory() 将会抛出异常。
createFile() 使用参数 Path 创建一个空文件; resolve() 将文件名添加到 test Path 的末尾。
 */













