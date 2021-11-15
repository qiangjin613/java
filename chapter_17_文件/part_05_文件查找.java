import java.io.IOException;
import java.nio.file.*;

/*
【文件查找】
到目前为止，为了找到文件，我们一直使用相当粗糙的方法，
在 path 上调用 toString()，然后使用 string 操作查看结果。

事实证明，java.nio.file 有更好的解决方案：
通过在 FileSystem 对象上调用 getPathMatcher() 获得一个 PathMatcher，然后传入您感兴趣的模式。
模式有两个选项：glob 和 regex。
glob 比较简单，实际上功能非常强大，因此您可以使用 glob 解决许多问题。
如果问题更复杂，可以使用 regex。
 */

/**
 * 使用 glob 查找以 .tmp 或 .txt 结尾的所有 Path：
 */
class Find {
    public static void main(String[] args) throws IOException {
        Path test = Paths.get("test");
        Directories.refreshTestDir();
        Directories.populateTestDir();
        /* 创建一个路径，而不是一个文件 */
        Files.createDirectory(test.resolve("dir.temp"));

        // glob 表达式开头的 **/ 表示“当前目录及所有子目录”
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher("glob:**/*.{temp,txt}");
        Files.walk(test)
                .filter(matcher::matches)
                .forEach(System.out::println);
        System.out.println("-- -- -- -- -- --");

        PathMatcher matcher2 = FileSystems.getDefault()
                .getPathMatcher("glob:*.temp");
        Files.walk(test)
                .map(Path::getFileName)
                .filter(matcher2::matches)
                .forEach(System.out::println);
        System.out.println("-- -- -- -- -- --");

        /*
        以上两种查找结果都包含文件或这目录，
        要只查找文件，必须像在下面代码中那样对其进行筛选。
         */

        Files.walk(test)
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .filter(matcher2::matches)
                .forEach(System.out::println);
    }
}
