import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/*
【文件系统】
为了完整，下面提供几种方法用于获取文件系统相关的其他信息：
1. 使用静态的 FileSystems 工具类获取"默认"的文件系统
2. 在 Path 对象上调用 getFileSystem() 以获取创建该 Path 的文件系统
3. 获得给定 URI 的文件系统
4. 还可以构建新的文件系统（对于支持它的操作系统）
 */
class FileSystemDemo {
    public static void main(String[] args) {
         System.out.println(System.getProperty("os.name"));
        FileSystem fsys = FileSystems.getDefault(); /* 获取默认的文件系统 */
        for (FileStore fs : fsys.getFileStores()) {
            System.out.println("File Store: " + fs);
        }
        for (Path rd : fsys.getRootDirectories()) {
            System.out.println("Root Directories: " + rd);
        }
        System.out.println("Separator: " + fsys.getSeparator());
        System.out.println("isOpen: " + fsys.isOpen());
        System.out.println("isReadOnly: " + fsys.isReadOnly());
        System.out.println("provider: " + fsys.provider());
        System.out.println("UserPrincipalLookupService: " + fsys.getUserPrincipalLookupService());
        System.out.println("supportedFileAttributeViews: " + fsys.supportedFileAttributeViews());
    }
}
/*
一个 FileSystem 对象也能生成 WatchService 和 PathMatcher 对象，
将会在接下来两节中详细讲解。
 */
