/*
【接口·嵌套】
接口可以嵌套在类或其他接口中。
 */

/**
 * 【类的内部接口】
 * 一些有趣的特性：
 */
class A {
    // 1.类的内部接口
    interface B {
        void f();
    }
    public class BImp implements B {
        @Override
        public void f() {}
    }
    public class BImp2 implements B {
        @Override
        public void f() {}
    }

    public interface C {
        void f();
    }
    class CImp implements C {
        @Override
        public void f() {}
    }
    private class CImp2 implements C {
        @Override
        public void f() {}
    }

    /* 作为一种新添加的方式，这种接口的权限修饰符并未进行限制 */
    private interface D {
        void f();
    }
    private class DImp implements D {
        @Override
        public void f() {}
    }
    /*  */
    public class DImp2 implements D {
        @Override
        public void f() {
            System.out.println("public class DImp2 的 f()");
        }
    }

    private D dRef;
    public D getD() {
        return new DImp2();
    }
    public void receive(D d) {
        dRef = d;
        dRef.f();
    }

    // 被各种修饰符修饰的内部接口：
    public interface InnerInterface {}
    protected interface InnerInterface2 {}
    interface InnerInterface3 {}
    private interface InnerInterface4 {}
}

/**
 * 【接口的内部接口】
 */
interface E {
    interface G {
        void f();
    }
    public interface H {
        void f();
    }
    public void g();

    // 编译器报错：非法的组合'protected' and 'public'
    // 这是因为接口中的东西，默认是 public 的
    // protected interface I {}
    // private interface L {}
}

class NestingInterfaces {
    /* 组合 class A 中的 interface B（B 是被 friendly 修饰的接口） */
    public class BImp implements A.B {
        @Override
        public void f() {}
    }

    /* A.C 是被 public 修饰的接口 */
    class CImp implements A.C {
        @Override
        public void f() {}
    }

    /* 不能组合 class A 中被 private 修饰的内部接口 A.D */
    // class DImp implements A.D {}

    /* 这里实现了接口 E，不用实现接口 E 内的其他接口 */
    class EImp implements E {
        @Override
        public void g() {}
    }
    class EGImp implements E.G {
        @Override
        public void f() {}
    }
    class EImp2 implements E {
        @Override
        public void g() {}
        /* 内部类的内部类*/
        class EG implements E.G {
            @Override
            public void f() {}
        }
    }

    public static void main(String[] args) {
        A a = new A();
        /* 即使有 public getD() 也不能使用 */
        // A.D ad = a.getD();

        /* 也不可以调用 D 中 public f() 的方法 */
        // a.getD().f();

        /* privte intreface D 的实现类 public A.DImp2 可以被获得 */
        A.DImp2 d = (A.DImp2) a.getD();
        d.f();

        /* 这样是可行的：使用 public 方法 */
        a.receive(a.getD());
    }
}
