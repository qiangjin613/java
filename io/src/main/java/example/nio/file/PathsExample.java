package example.nio.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 示例
 */
public class PathsExample {

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        Path path = Paths.get("PathsExample.java").toAbsolutePath();
        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println(path.getName(i));
        }
        System.out.println("ends with '.java': " + path.endsWith(".java"));
        for (Path p : path) {
            System.out.println(p + ": " + path.startsWith(p) + ": " + path.endsWith(p));
        }
        System.out.println("Starts with " + path.getRoot() + " " + path.startsWith(path.getRoot()));
    }


    public static void main2(String[] args) {
        System.out.println(System.getProperty("os.name"));
        info(Paths.get("D:", "path", "to", "nowhere", "NoFile.txt"));
        Path path = Paths.get("PathsExample.java");
        info(path);
        Path ap = path.toAbsolutePath();
        info(ap);
        info(ap.getParent());
        try {
            info(path.toRealPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        URI uri = path.toUri();
        System.out.println("URI: " + uri);
        Path puri = Paths.get(uri);
        System.out.println(Files.exists(puri));
        File file = ap.toFile(); // ?
        System.out.println(file.getName());
    }

    static void show(String id, Object p) {
        System.out.println(id + p);
    }

    static void info(Path p) {
        show("toString: ", p);
        show("Exists: ", Files.exists(p));
        show("RegularFile: ", Files.isRegularFile(p));
        show("Directory: ", Files.isDirectory(p));
        show("Absolute: ", p.isAbsolute());
        show("FileName: ", p.getFileName());
        show("Parent: ", p.getParent());
        show("Root: ", p.getRoot());
        System.out.println("****************");
    }
}
