/*
【文件和目录路径】
一个 Path 对象表示一个文件或者目录的路径，
是一个跨操作系统（OS）和文件系统的抽象，
目的是在构造路径时不必关注底层操作系统，代码可以在不进行修改的情况下运行在不同的操作系统上。
 */

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Paths、Path、Files 示例：
 */
class PathInfo {

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name")); // Output: Windows 10
        info(Paths.get("C:", "path", "to", "nowhere", "NoFile.txt"));
        // 这里获取不到文件
        // Path p = Paths.get("part_01_文件和目录路径.java");
        // 使用这个可以获取到文件：
        Path p = Paths.get("chapter_17_文件", "part_01_文件和目录路径.java");
        info(p);

        // 转换未绝对路径：
        Path ap = p.toAbsolutePath();
        info(ap);
        info(ap.getParent());

        try {
            info(p.toRealPath());
        } catch (IOException e) {
            System.out.println(e);
        }

        URI u = p.toUri();
        System.out.println("URI: " + u);
        Path puri = Paths.get(u);
        System.out.println(Files.exists(puri));

        File f = ap.toFile();
    }

    static void info(Path p) {
        show("toString", p);
        show("Exists", Files.exists(p));
        show("RegularFile", Files.isRegularFile(p));
        // 判断是一个路径还是文件：
        show("Directory", Files.isDirectory(p));
        show("Absolute", p.isAbsolute());
        show("FileName", p.getFileName());
        show("Parent", p.getParent());
        show("Root", p.getRoot());
        System.out.println("--- --- --- ---");
    }

    static void show(String desc, Object p) {
        System.out.println(desc + ": " + p);
    }
}
/*
在 Path 中看到一些有点欺骗的东西，这就是调用 toFile() 方法会生成一个 File 对象。
听起来似乎可以得到一个类似文件的东西(毕竟被称为 File )，
但是这个方法的存在仅仅是为了向后兼容。
虽然看上去应该被称为"路径"，实际上却应该表示目录或者文件本身。
这是个非常草率并且令人困惑的命名，但是由于 java.nio.file 的存在我们可以安全地忽略它的存在。
 */



/*
【选取路径部分片段】
Path 对象可以非常容易地生成路径的某一部分。
 */
class PartsOfPaths {
    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        /* Output:
        Windows 10
         */

        Path p = Paths.get("chapter_17_文件", "part_01_文件和目录路径.java").toAbsolutePath();
        PathInfo.info(p);
        /* Output:
        toString: F:\PrivateData\Notes\java-notes\chapter_17_文件\part_01_文件和目录路径.java
        Exists: true
        RegularFile: true
        Directory: false
        Absolute: true
        FileName: part_01_文件和目录路径.java
        Parent: F:\PrivateData\Notes\java-notes\chapter_17_文件
        Root: F:\
         */

        // 拆解目录
        System.out.println("拆解目录");
        for (int i = 0; i < p.getNameCount(); i++) {
            System.out.println(p.getName(i));
        }
        /* Output:
        PrivateData
        Notes
        java-notes
        chapter_17_文件
        part_01_文件和目录路径.java
         */

        System.out.println("ends with '.java': " + p.endsWith(".java"));
        /* Output:
        ends with '.java': false
         */

        for (Path pp : p) {
            System.out.println(pp + ": " + p.startsWith(pp) + " : " + p.endsWith(pp));
        }
        /* Output:
        PrivateData: false : false
        Notes: false : false
        java-notes: false : false
        chapter_17_文件: false : false
        part_01_文件和目录路径.java: false : true
         */

        System.out.println("Starts with " + p.getRoot() + " " + p.startsWith(p.getRoot()));
        /* Output:
        Starts with F:\ true
         */
    }
}
/*
可以通过 getName() 来索引 Path 的各个部分，直到达到上限 getNameCount()。
Path 也实现了 Iterable 接口，因此我们也可以通过增强的 for-each 进行遍历。

注意，即使路径以 .java 结尾，使用 endsWith() 方法也会返回 false。
这是因为使用 endsWith() 比较的是整个路径部分，而不会包含文件路径的后缀。

通过使用 startsWith() 和 endsWith() 也可以完成路径的遍历。
但是我们可以看到，遍历 Path 对象并不包含根路径，只有使用 startsWith() 检测根路径时才会返回 true。
 */



/*
【路径分析】
Files 工具类包含一系列完整的方法用于获得 Path 相关的信息。
 */
class PathAnalysis {
    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("os.name"));
        Path p = Paths.get("chapter_17_文件", "part_01_文件和目录路径.java").toAbsolutePath();
        PathInfo.info(p);
        PathInfo.show("Exists", Files.exists(p));
        PathInfo.show("Directory", Files.isDirectory(p));
        PathInfo.show("Executable", Files.isExecutable(p));
        PathInfo.show("Readable", Files.isReadable(p));
        PathInfo.show("RegularFile", Files.isRegularFile(p));
        PathInfo.show("Writable", Files.isWritable(p));
        PathInfo.show("notExists", Files.notExists(p));
        PathInfo.show("Hidden", Files.isHidden(p));
        PathInfo.show("size", Files.size(p));
        PathInfo.show("FileStore", Files.getFileStore(p));
        PathInfo.show("LastModifiedTime", Files.getLastModifiedTime(p));
        PathInfo.show("Owner", Files.getOwner(p));
        PathInfo.show("ContentType", Files.probeContentType(p));
        PathInfo.show("SymbolicLink", Files.isSymbolicLink(p));
        if (Files.isSymbolicLink(p))
            PathInfo.show("readSymbolicLink", Files.readSymbolicLink(p));
        // 需要确认一下当前文件系统是否支持 Posix 接口，否则会抛出运行时异常：
        if(FileSystems.getDefault().supportedFileAttributeViews().contains("posix"))
            PathInfo.show("PosixFilePermissions", Files.getPosixFilePermissions(p));
    }
}



/*
【Paths的增减修改】
我们必须能通过对 Path 对象增加或者删除一部分来构造一个新的 Path 对象。
我们使用 relativize() 移除 Path 的根路径，
使用 resolve() 添加 Path 的尾路径(不一定是“可发现”的名称)。
 */
class AddAndSubtractPaths {
    static Path base = Paths.get("..", "..", "..").toAbsolutePath().normalize();

    static void show(int id, Path result) {
        if (result.isAbsolute()) {
            System.out.println("(" + id + ")r " + base.relativize(result));
        } else {
            System.out.println("(" + id + ")r " + result);
        }
        try {
            System.out.println("RealPath: " + result.toRealPath());
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("--- --- --- ---");
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        System.out.println(base);
        Path p = Paths.get("chapter_17_文件", "part_01_文件和目录路径.java").toAbsolutePath();
        show(1, p);

        Path convoluted = p.getParent().getParent()
                .resolve("strings")
                .resolve("..")
                .resolve(p.getParent().getFileName());
        show(2, convoluted);
        show(3, convoluted.normalize());

        Path p2 = Paths.get("..", "..");
        show(4, p2);
        show(5, p2.normalize());
        show(6, p2.toAbsolutePath().normalize());

        Path p3 = Paths.get(".").toAbsolutePath();
        show(7, p3);

        Path p4 = p3.resolve(p2);
        show(8, p4);
        show(9, p4.normalize());

        Path p5 = Paths.get("").toAbsolutePath();
        show(10, p5);
        show(11, p5.resolveSibling("strings"));

        show(12, Paths.get("nonexistent"));
    }
}
