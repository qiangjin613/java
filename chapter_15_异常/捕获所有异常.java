import java.util.Arrays;

/**
 * 输出异常信息
 */
class ExceptionMethods {
    public static void main(String[] args) {
        try {
            throw new Exception("My Exception");
        } catch (Exception e) {
            System.out.println("===== Caught Exception =====");
            // 输出的信息越来越多
            System.out.println("getMessage(): " + e.getMessage()); // output: 'msg'
            System.out.println("getLocalizedMessage(): " + e.getLocalizedMessage()); // output: 'msg'
            System.out.println("toString(): " + e); // output: 'className: msg'
            e.printStackTrace();
            e.printStackTrace(System.out);
        }
    }
}


/**
 * 【多重捕获】
 */
// 构建一批没有共同类型的类
class EBase1 extends Exception {}
class Except1 extends EBase1 {}
class EBase2 extends Exception {}
class Except2 extends EBase2 {}
class EBase3 extends Exception {}
class Except3 extends EBase3 {}
class EBase4 extends Exception {}
class Except4 extends EBase4 {}

class SameHandler {
    void x() throws Except1, Except2, Except3, Except4 {}
    void f() {
        try {
            x();
        }
        // 如果这些异常没有用共同的基类型，在 Java 7 之前，必须每个类型捕获一次
        catch (Except1 e) {
            e.printStackTrace();
        } catch (Except2 e) {
            e.printStackTrace();
        } catch (Except3 e) {
            e.printStackTrace();
        } catch (Except4 e) {
            e.printStackTrace();
        }
    }
}
class MultiCatch {
    void x() throws Except1, Except2, Except3, Except4 {}
    void f() {
        try {
            x();
        }
        // Java 7 的多重捕获机制，可以使用 '|' 将不同类型的异常组合起来进行捕获
        // 帮助编写整洁的代码
        catch (Except1 | Except2 | Except3 | Except4 e) {
            e.printStackTrace();
        }
    }
}
class MultiCatch2 {
    void x() throws Except1, Except2, Except3, Except4 {}
    void f() {
        try {
            x();
        }
        // Java 7 的多重捕获机制，可以使用 '|' 将不同类型的异常组合起来进行捕获
        catch (Except1 | Except2 e) {
            e.printStackTrace();
        } catch (Except3 | Except4 e) {
            e.printStackTrace();
        }
    }
}


/**
 * 【栈轨迹】
 */
class WhoCalled {
    static void f() {
        try {
            throw new Exception();
        } catch (Exception e) {
            for (StackTraceElement ste : e.getStackTrace()) {
                System.out.println("ste.getMethodName() = " + ste.getMethodName() + ";\t"
                        + "ste.toString() = " + ste);
            }
        }
    }
    static void g() { f(); }
    static void h() { g(); }

    public static void main(String[] args) {
        // 栈顶都是 f，栈底都是 main
        // main 是程序的起点，f 是异常抛出的位置
        f(); // f --> main
        System.out.println("=======================");
        g(); // f --> g --> main
        System.out.println("=======================");
        h(); // // f --> g --> h --> main
        System.out.println("=======================");
    }
}


/**
 * 【重新抛出异常】
 */
class Rethrowing {
    public static void f() throws Exception {
        System.out.println("originating the exception in f()");
        throw new Exception("thrown from f()");
    }
    public static void g() throws Exception {
        try {
            f();
        } catch (Exception e) {
            System.out.println("Inside g(), e.printStackTrace()");
            e.printStackTrace(System.out);
            throw e; // 将捕获的异常再次抛出，异常栈信息不变
        }
    }
    public static void h() throws Exception {
        try {
            f();
        } catch (Exception e) {
            System.out.println("Inside h(), e.printStackTrace()");
            e.printStackTrace();
            throw (Exception) e.fillInStackTrace(); // 把当前调用栈信息填入 e 异常对象中，抛出的异常栈将从这里开始（这里就是异常的新发地）
        }
    }

    public static void main(String[] args) {
        try {
            g();
        } catch (Exception e) {
            System.out.println("main: g(): printStackTrace()");
            e.printStackTrace(System.out);
        }
        try {
            h();
        } catch (Exception e) {
            System.out.println("main: h(): printStackTrace()");
            e.printStackTrace(System.out);
        }
    }
}

// 捕获异常后抛出另一种异常
class OneException extends Exception {
    OneException(String msg) { super(msg); }
}
class TwoException extends Exception {
    TwoException(String msg) { super(msg); }
}

class RethrowNew {
    public static void f() throws OneException {
        System.out.println("originating the exception in f()");
        throw new OneException("thrown from f()");
    }

    public static void main(String[] args) {
        try {
            try {
                f();
            } catch (OneException e) {
                System.out.println("Caught in inner try, e.printStackTrace()");
                e.printStackTrace(System.out);
                throw new TwoException("from inner try"); // 抛出另一种异常，会丢失之前的异常信息
            }
        } catch (TwoException e) {
            System.out.println("Caught in outer try, e.printStackTrace()");
            e.printStackTrace(System.out);
        }
    }
}


/**
 * 【精准的重新抛出异常】
 * since 1.7
 */
class BaseException extends Exception {}
class DerivedException extends BaseException {}
// catch 不捕获了一个 BaseException，实际上抛出更具体的 DerivedException
class PreciseRethrow {
    void catcher() throws DerivedException {
        try {
            throw new DerivedException();
        } catch (BaseException e) {
            throw e;
        }
    }
}


/**
 * 【异常链】
 */
// 自定义异常
class DynamicFieldsException extends Exception {
    DynamicFieldsException() {}
    // 使用 Exception(Throwable cause) 构造器构建异常链
    // 也可以使用 异常对象.initCause(Throwable cause) 方法构建异常链
    DynamicFieldsException(Throwable cause) { super(cause); }
}
// 在运行过程中动态地向 DynamicFields 对象添加字段
class DynamicFields {
    private Object[][] fields; // fields[0] -- key; fields[1] -- value

    public DynamicFields(int initialSize) {
        fields = new Object[initialSize][2];
        for (int i = 0; i < initialSize; i++) {
            fields[i] = new Object[] { null, null };
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Object[] obj : fields) {
            result.append(obj[0]);
            result.append(": ");
            result.append(obj[1]);
            result.append("\n");
        }
        return result.toString();
    }

    // 查找 fields 中，fields[i][0] 为 id 的 index
    private int hasField(String id) {
        for (int i = 0; i < fields.length; i++) {
            if (id.equals(fields[i][0])) {
                return i;
            }
        }
        return -1;
    }

    private int getFieldNumber(String id) throws NoSuchFieldException {
        int fieldNum = hasField(id);
        if (fieldNum == -1) {
            throw new NoSuchFieldException();
        }
        return fieldNum;
    }

    private int makeField(String id) {
        for (int i = 0; i < fields.length; i++) {
            if (fields[i][0] == null) {
                fields[i][0] = id;
                return i;
            }
        }
        // 如果字段都用完了，增加一个（扩容）
        Object[][] tmp = new Object[fields.length + 1][2];
        for (int i = 0; i < fields.length; i++) {
            tmp[i] = fields[i];
        }
        for (int i = fields.length; i < tmp.length; i++) {
            tmp[i] = new Object[] { null, null };
        }
        fields = tmp;
        // 再次将 id 插入进去
        return makeField(id);
    }

    public Object getField(String id) throws NoSuchFieldException {
        return fields[getFieldNumber(id)][1];
    }

    public Object setField(String id, Object value) throws DynamicFieldsException {
        if (value == null) {
            DynamicFieldsException dfe = new DynamicFieldsException();
            // DynamicFieldsException dfe = new DynamicFieldsException(new NullPointerException());
            dfe.initCause(new NullPointerException());
            throw dfe;
        }
        int fieldNumber = hasField(id);
        if (fieldNumber == -1) {
            fieldNumber = makeField(id);
        }
        Object result = null;
        try {
            result = getField(id);
        } catch (NoSuchFieldException e) {
            // 使用带"cause"参数的构造函数:
            throw new RuntimeException(e);
        }
        fields[fieldNumber][1] = value;
        return result;
    }

    public static void main(String[] args) {
        DynamicFields df = new DynamicFields(3);
        System.out.println(df);
        try {
            df.setField("d", "A value for d");
            df.setField("number", 47);
            df.setField("number2", 48);
            System.out.println(df);

            df.setField("d", "A new value for d");
            df.setField("number3", 20);
            System.out.println("df:" + df);

            System.out.println("df.getField(\"d\")" + df.getField("d"));

            // 抛出异常：
            Object field = df.setField("d", null);
        } catch (DynamicFieldsException | NoSuchFieldException e) {
            e.printStackTrace(System.out);
        }
    }
}