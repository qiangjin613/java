import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/*
【路径监听】
通过 WatchService 可以设置一个进程对目录中的更改做出响应。
 */

/**
 * 在这个例子中，delTxtFiles() 作为一个单独的任务执行，
 * 该任务将遍历整个 test 目录并删除以 .txt 结尾的所有文件，
 * WatchService 会对文件删除操作做出反应：
 */
class PathWatcher {
    static Path test = Paths.get("test");

    static void delTextFiles() {
        try {
            Files.walk(test)
                    .filter(f -> f.toString().endsWith(".txt"))
                    .forEach(f -> {
                        try {
                            System.out.println("deleting " + f);
                            Files.delete(f);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Directories.refreshTestDir();
        Directories.populateTestDir();
        Files.createFile(test.resolve("Hello.txt"));

        /*
        FileSystem 中得到了 WatchService 对象，
        将其注册到 test 路径以及我们感兴趣的项目的变量参数列表中，
        可以选择 ENTRY_CREATE，ENTRY_DELETE 或 ENTRY_MODIFY(其中创建和删除不属于修改)
        */
        WatchService watcher = FileSystems.getDefault().newWatchService();
        test.register(watcher, ENTRY_DELETE);

        /*
        创建一个线程，在一定的延迟后执行 delTextFiles 方法
        */
        Executors.newSingleThreadScheduledExecutor()
                .schedule(PathWatcher::delTextFiles, 250, TimeUnit.MILLISECONDS);

        /*
        watcher.take() 将等待并阻塞在这里。
        当目标事件发生时，会返回一个包含 WatchEvent 的 Watchkey 对象。
        展示的这三种方法是能对 WatchEvent 执行的全部操作。
        */
        WatchKey key = watcher.take();
        for (WatchEvent<?> evt : key.pollEvents()) {
            System.out.println("evt.context(): " + evt.context() +
                    "\nevt.count(): " + evt.count() +
                    "\nevt.kind(): " + evt.kind());
            System.exit(0);
        }
    }
}
/* Output:
deleting test\bag\foo\bar\baz\File.txt
deleting test\bar\baz\bag\foo\File.txt
deleting test\baz\bag\foo\bar\File.txt
deleting test\foo\bar\baz\bag\File.txt
deleting test\Hello.txt
evt.context(): Hello.txt
evt.count(): 1
evt.kind(): ENTRY_DELETE

查看输出的具体内容。即使我们正在删除以 .txt 结尾的文件，
在 Hello.txt 被删除之前，WatchService 也不会被触发。
 */

/*
可能认为，如果说"监视这个目录"，自然会包含整个目录和下面子目录，
但实际上：只会监视给定的目录，而不是下面的所有内容。
如果需要监视整个树目录，必须在整个树的每个子目录上放置一个 Watchservice。
 */

class TreeWatcher {
    static void watchDir(Path dir) {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_DELETE);
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    WatchKey key = watcher.take();
                    for (WatchEvent<?> evt : key.pollEvents()) {
                        System.out.println("evt.context(): " + evt.context() +
                                "\nevt.count(): " + evt.count() +
                                "\nevt.kind(): " + evt.kind());
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Directories.refreshTestDir();
        Directories.populateTestDir();
        /* 遍历整个目录树，并将 watchDir() 应用于每个子目录 */
        Files.walk(Paths.get("test"))
                .filter(Files::isDirectory)
                .forEach(TreeWatcher::watchDir);
        /* 当我们运行 deltxtfiles() 时，其中一个 Watchservice 会检测到每一次文件删除 */
        PathWatcher.delTextFiles();
    }
}
/* Output:
deleting test\bag\foo\bar\baz\File.txt
evt.context(): File.txt
evt.count(): 1
evt.kind(): ENTRY_DELETE

只输出一个是因为在线程中的 System.exit(0);
如果没有 System.exit(0); 那这个线程将一直进行下去。
 */
