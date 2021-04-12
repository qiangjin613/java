import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class InputFile {
    private BufferedReader in;
    public InputFile(String fname) throws Exception {
        try {
            in = new BufferedReader(new FileReader(fname));
        } catch (FileNotFoundException e) {
            System.out.println("Could not open " + fname);
            throw e;
        } catch (Exception e) {
            try {
                in.close();
            } catch (IOException e2) {
                System.out.println("in.close() unsuccessful");
            }
            throw e;
        } finally {
            // 不可以在这里使用 in.close()
        }
    }
    public String getLine() {
        String s;
        try {
            s = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("readLine() failed");
        }
        return s;
    }
    public void dispose() {
        try {
            in.close();
            System.out.println("dispose() successful");
        } catch (IOException e) {
            throw new RuntimeException("in.close() failed");
        }
    }
}

class Cleanup {
    public static void main(String[] args) {
        try {
            InputFile in = new InputFile("构造器.java");
            // 在构造器构造成功后，想确保对象能被清理。
            // 因此，建立了一个新的 try 语句块。执行清理的 finally 与内部的 try 语句块相关联。
            // finally 子句在 InputFile 构造器构造失败是不会执行的，而在构造器构造成功时将总是执行
            try {
                String s;
                int i = 1;
                while ((s = in.getLine()) != null) {}
            } catch (Exception e) {
                System.out.println("Caught Exception in main");
                e.printStackTrace(System.out);
            } finally {
                in.dispose(); // 只有在 in 对象构建成功后，进入到 try 块里面才会执行
            }
        } catch (Exception exception) {
            System.out.println("InputFile construction failed");
        }
    }
}

/**
 * 在创建需要清理的对象之后，立即进入一个 try-finally 语句块
 */
class NeedsCleanup {
    private static long counter = 1;
    private final long id = counter++;
    // 构造方法不可以失败
    NeedsCleanup() {}
    public void dispose() {
        System.out.println("NeedsCleanup " + id + " disposed");
    }
}
class ConstructionException extends Exception {}
class NeedsCleanup2 extends NeedsCleanup {
    // 构造方法可以失败
    NeedsCleanup2() throws ConstructionException {}
}
class CleanupIdiom {
    public static void main(String[] args) {
        // [1] 遵循了在可去除对象之后紧跟 try-finally 的原则。如果对象构造不会失败，就不需要任何 catch
        NeedsCleanup nc1 = new NeedsCleanup();
        try {
            // do sth.
        } finally {
            nc1.dispose();
        }

        // [2]
        // 如果构造对象不可能失败，可以将这些放在一起：
        NeedsCleanup nc2 = new NeedsCleanup();
        NeedsCleanup nc3 = new NeedsCleanup();
        try {
            // do sth.
        } finally {
            // 顺序创建，倒叙关闭
            nc3.dispose();
            nc2.dispose();
        }

        // [3] 展示了如何处理那些具有可以失败的构造器，且需要清理的对象：（为了正确处理这种情况，事情变得很棘手）
        // 如果构造对象可能失败，必须保护每一个：
        //      因为对于每一个构造，都必须包含在其自己的 try-finally 语句块中，
        //      并且每一个对象构造必须都跟随一个 try-finally 语句块以确保清理
        try {
            NeedsCleanup2 nc4 = new NeedsCleanup2();
            try {
                NeedsCleanup2 nc5 = new NeedsCleanup2();
                try {
                    // do sth.
                } finally {
                    nc5.dispose();
                }
            } catch (ConstructionException e) {
                System.out.println(e);
            } finally {
                nc4.dispose();
            }
        } catch (ConstructionException e) {
            System.out.println(e);
        }
    }
}