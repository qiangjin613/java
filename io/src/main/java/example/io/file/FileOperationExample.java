package example.io.file;

import java.io.File;
import java.io.IOException;

/**
 * java.io.File 使用示例，主要包含：
 *
 * <ul>
 * <li> 文件操作：创建、删除、获取文件信息
 * <li> 目录操作：创建、删除
 * </ul>
 *
 * NOTE：
 *
 * <ul>
 * <li> createNewFile()/mkdir()/mkdirs 创建存在的文件/目录时，返回 false。
 * <li> 使用 java.io.File#delete() 删除目录时，不能直接删除嵌套目录，要从最内层开始删除才行。
 * </ul>
 *
 * @author  qiangj
 */
public class FileOperationExample {
    public static void main(String[] args) {
        // ------------------ 文件操作 ------------------
        createNewFile();
        printFileInfo();
        deleteFile();
        // ------------------ 目录操作 ------------------
        createDirectory();
        printDirectoryInfo();
        deleteDirectory();
    }

    /**
     * 创建文件
     *
     * <ol>
     * <li> 对于 new File 对象，仅是创建了一个对象，并未生成文件
     * <li> 具体生成文件在 file.createNewFile() 中进行
     * </ol>
     */
    public static void createNewFile() {
        // 方式一：通过将给定的路径名字符串转换为抽象路径名来创建一个新的 File 实例
        File file = new File("D:\\testFile1.txt");
        try {
            // 创建文件
            if (file.createNewFile()) {
                System.out.println("文件 ”D:\\testFile1.txt“ 创建成功");
            } else {
                System.out.println("文件 ”D:\\testFile1.txt“ 创建失败");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 方式二：从父抽象路径名和子路径名字符串创建一个新的 File 实例
        File parentFile = new File("D:\\");
        File file2 = new File(parentFile, "testFile2.txt");
        try {
            if (file2.createNewFile()) {
                System.out.println("文件 ”D:\\testFile2.txt“ 创建成功");
            } else {
                System.out.println("文件 ”D:\\testFile2.txt“ 创建失败");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 方式三：从父路径名字符串和子路径名字符串创建一个新的 File 实例
        File file3 = new File("D:\\", "testFile3.txt");
        try {
            if (file3.createNewFile()) {
                System.out.println("文件 ”D:\\testFile3.txt“ 创建成功");
            } else {
                System.out.println("文件 ”D:\\testFile3.txt“ 创建失败");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 方式四：通过将给定的File: URI 转换为抽象路径名来创建一个新的 File 实例

    }

    /**
     * 获取文件相关信息
     */
    public static void printFileInfo() {
        File file = new File("D:\\testFile1.txt");

        System.out.println("Name: " + file.getName() + "\n"
                + "Path: " + file.getPath() + "\n"
                + "AbsolutePath: " + file.getAbsolutePath() + "\n"
                + "Parent: " + file.getParent() + "\n"
                + "length: " + file.length() + "\n"
                + "exists: " + file.exists() + "\n"
                + "isFile: " + file.isFile() + "\n"
                + "isDirectory: " + file.isDirectory() + "\n"
                + "isHidden: " + file.isHidden() + "\n... 等等");
    }

    /**
     * 删除文件
     */
    public static void deleteFile() {
        File file = new File("D:\\testFile1.txt");
        // 当文件存在时删除文件
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("testFile1.txt delete success");
            } else {
                System.out.println("testFile1.txt delete failure");
            }
        }
    }

    /**
     * 创建目录
     */
    public static void createDirectory() {
        // 创建单层目录
        File file = new File("D:\\dir007");
        if (file.mkdir()) {
            System.out.println("目录 D:\\dir007 创建成功");
        } else {
            System.out.println("目录 D:\\dir007 创建失败");
        }

        // 创建嵌套目录
        File file2 = new File("D:\\dir007\\abc\\abc\\aaa\\ccc");
        if (file2.mkdirs()) {
            System.out.println("目录 D:\\dir007\\abc\\abc\\aaa\\ccc 创建成功");
        } else {
            System.out.println("目录 D:\\dir007\\abc\\abc\\aaa\\ccc 创建失败");
        }
    }

    /**
     * 输出目录信息，在 Java 编程中，”目录“也是”文件“。
     */
    public static void printDirectoryInfo() {
        File file = new File("D:\\dir007\\abc\\abc\\aaa\\ccc");
        System.out.println("Name: " + file.getName() + "\n"
                + "Path: " + file.getPath() + "\n"
                + "AbsolutePath: " + file.getAbsolutePath() + "\n"
                + "Parent: " + file.getParent() + "\n"
                + "length: " + file.length() + "\n"
                + "exists: " + file.exists() + "\n"
                + "isFile: " + file.isFile() + "\n"
                + "isDirectory: " + file.isDirectory() + "\n"
                + "isHidden: " + file.isHidden() + "\n... 等等");
    }

    /**
     * 删除目录
     */
    public static void deleteDirectory() {
        File file = new File("D:\\dir007");
        // 当目录存在时删除文件
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Directory \"D:\\dir007\" delete success");
            } else {
                System.out.println("Directory \"D:\\dir007\" delete failure");
            }
        }
    }
}
