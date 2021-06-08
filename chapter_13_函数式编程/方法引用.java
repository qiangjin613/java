/**
 * 简单示例
 */
interface Callable {
    void call(String s);
}

class Describe {
    void show(String msg) {
        System.out.println("Describe.show(): " + msg);
    }
    static void hello(String name) {
        System.out.println("Describe.static hello(): " + name);
    }
}

class MethodReferences {

    class Helper {
        void assit(String msg) {
            System.out.println("inner class Helper.assit(): " + msg);
        }
    }

    static class Description {
        String about;
        Description(String desc) {
            about = desc;
        }
        void hello(String msg) {
            System.out.println("inner static class Description.hello(): " + about + " " + msg);
        }
        void help(String msg) {
            System.out.println("inner static class Description.help(): " + about + " " + msg);
        }
        static void assist(String msg) {
            System.out.println("inner static class Description.static assist(): " + msg);
        }
    }


    public static void main(String[] args) {
        Callable c;

        // [1] 绑定普通类的普通方法
        Describe d = new Describe();
        c = d::show;
        /* 也可以 */
        // c = new Describe()::show;
        c.call("d::show");

        // [2] 绑定普通类的静态方法
        c = Describe::hello;
        c.call("d.hello");
        /* 报错：通过非静态限定符引用的静态方法 */
        // c = d::hello;

        // [3] 绑定内部类的普通方法
        c = new MethodReferences().new Helper()::assit;
        c.call("assit");

        // [4] 绑定嵌套类的普通方法
        Description innerD = new Description("new Desc");
        c = innerD::hello;
        c.call("hello");
        c = innerD::help;
        c.call("help");

        // [5] 绑定嵌套类的静态方法
        c = Description::assist;
        c.call("assist");
    }
}

/*
发现：
1. Callable 接口只有一个方法
2. 方法签名相同，就可以进行绑定。
    如：show() 的签名（参数类型和返回类型）符合 Callable 的 call() 的签名。
 */


/**
 * 又一个例子：Runnable接口
 */
class Go {
    static void go() {
        System.out.println("Go::go");
    }
}
class RunnableMethodReference {
    public static void main(String[] args) {
        // [1] 匿名内部类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anon");
            }
        }).start();

        // [2] Lambda
        new Thread(() -> System.out.println("Lambda")).start();

        // [3] 方法绑定
        new Thread(Go::go).start();
    }
}



/*
【未绑定的方法引用】
 */
class XX {
    String f() {
        return "XX::F()";
    }
}

interface MakeString {
    String make();
}

interface TransformXX {
    String transform(XX x);
}

class UnboundMethodReference {
    public static void main(String[] args) {
        /*
        [1] 这里报错是因为还需要另一个隐藏参数 this，
        不能在没有 XX 对象的前提下调用 f()。
        因此，XX::f 表示未绑定的方法引用，因为它尚未绑定到对象。
        如果要使用，可以：MakeString ms = new XX()::f;
         */
        // MakeString ms = XX::f;

        /*
        要解决“未绑定的方法引用”的问题，我们就需要一个 XX 的对象，
        因此，接口实际上需要一个额外的参数，正如 TransformXX.transform(XX x) 方法。
         */
        TransformXX sp = XX::f;
        /*
        在这里，必须要进行第二个语法介绍：
        使用未绑定的引用时，
        函数式方法的签名（接口中的单个方法）不再与方法引用的签名完全匹配。
        原因是：需要一个对象来调用方法。
         */
        XX x = new XX();
        System.out.println(x.f());

        // [2] 调用方法
        System.out.println(sp.transform(x));
    }
}

/*
新的问题又来了：
    如果未绑定的方法引用有更多的参数时怎么解决？
Q：就以第一个参数接受this的模式来处理。

例子：未绑定的方法与多参数的结合运用
 */
class This {
    void two(int i, double d) {}
    void three(int i, double d, String s) {}
}

interface TwoArgs {
    void call2(This athis, int i, double d);
}
interface ThreeArgs {
    void call3(This athis, int i, double d, String s);
}

class MultiUnbound {
    public static void main(String[] args) {
        TwoArgs t2 = This::two;
        ThreeArgs t3 = This::three;

        This thi = new This();
        t2.call2(thi, 1, 1.0d);
        t3.call3(thi, 1, 2.0d, "hello");
    }
}



/*
【构造函数引用】
 */
class Dogg {
    String name;
    int age = -1;
    Dogg() {
        name = "stray";
    }
    Dogg(String nm) {
        name = nm;
    }
    Dogg(String nm, int yrs) {
        name = nm;
        age = yrs;
    }
}

interface MakeNoArgs {
    Dogg make();
}
interface Make1Args {
    Dogg make(String nm);
}
interface Make2Args {
    Dogg make(String nm, int age);
}

class CtorReference {
    public static void main(String[] args) {
        // [1] 绑定构造器（编译器知道使用的是哪个构造函数）
        MakeNoArgs mna = Dogg::new;
        Make1Args m1a = Dogg::new;
        Make2Args m2a = Dogg::new;

        // [2] 调用方法就相当于调用构造器
        Dogg dn = mna.make();
        Dogg d1 = m1a.make("dog1");
        Dogg d2 = m2a.make("dog2", 1);
    }
}
