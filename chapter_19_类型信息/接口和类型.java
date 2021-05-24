import 接口和类型.A;
import 接口和类型.HiddenC;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
接口的耦合
 */
class B implements A {
    @Override
    public void f() {}
    public void g() {}
}
class InterfaceViolation {
    public static void main(String[] args) {
        A a = new B();
        a.f();
        System.out.println(a.getClass().getName()); // output: B
        /*
        虽然对象 a 没有 g() 方法，但是我们可以通过向下转型获得 B 对象，从而调用 g()
         */
        if (a instanceof B) {
            B b = (B) a;
            b.g();
        }
    }
}
/*
在上述示例中，通过 RTTI 发现 a 是用 B 实现的。
通过将其转型为 B，我们可以调用不在 A 中的方法。

这样的操作完全是合情合理的，但是你也许并不想让客户端开发者这么做，
因为这给了他们一个机会，使得他们的代码与你的代码的耦合度超过了你的预期。
 */

/*
对于接口耦合度的解决方案，一种是直接声明。
一种是用一种更严格的控制方式：让实现类只具有包访问权限，
这样在包外部的客户端就看不到它了。
如：使用 “package 接口和类型” 中的 “HiddenC” 类

如果你试着将其向下转型为 C，则将被禁止，因为在包的外部没有任何 C 类型可用
如下：
 */
class HiddenImplementation {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        A a = HiddenC.makeA();
        a.f();
        System.out.println(a.getClass().getName()); // out: 接口和类型.C
        // 不能从外部包访问 C:
//        if (a instanceof C) {
//
//        }
        callHiddenMethod(a, "g");
        callHiddenMethod(a, "u");
        callHiddenMethod(a, "v");
        callHiddenMethod(a, "w");
        System.out.println("---------- callAllMethod ------------");
        callAllMethod(a);
    }
    static void callHiddenMethod(Object a, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = a.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        method.invoke(a);
    }
    static void callAllMethod(Object a) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = a.getClass().getDeclaredMethods();
        for (Method m : methods) {
            m.setAccessible(true);
            m.invoke(a);
        }
    }
}
/*
在上述代码中，通过反射，仍然可以调用所有方法（甚至是 private 方法）。

另外：在 .class 对应的文件夹下，
使用 "javap -private 类名" 命令，可以看到这个受保护类的所有方法，包括 private 方法！

【因此，任何人都可以获取你最私有的方法的名字和签名，然后调用它们】
 */

/*
在下面对 私有内部类、匿名内部类、私有字段 做出测试：
 */
/**
 * 私有内部内的测试
 */
class InnerA {
    private static class C implements A {
        @Override
        public void f() {
            System.out.println("私有内部内：public C.f()");
        }
        public void g() {
            System.out.println("私有内部内：public C.g()");
        }
        void u() {
            System.out.println("私有内部内：package C.u()");
        }
        protected void v() {
            System.out.println("私有内部内：protected C.v()");
        }
        private void w() {
            System.out.println("私有内部内：private C.w()");
        }
    }

    public static A makeA() {
        return new C();
    }
}
class InnerImplementation {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        A a = InnerA.makeA();
        a.f();
        System.out.println(a.getClass().getName()); // out: InnerA$C
        // 调用私有内部内的全部方法：
        HiddenImplementation.callHiddenMethod(a, "g");
        HiddenImplementation.callHiddenMethod(a, "u");
        HiddenImplementation.callHiddenMethod(a, "v");
        HiddenImplementation.callHiddenMethod(a, "w");
    }
}


/**
 * 匿名内部类的测试
 */
class AnonymousA {
    public static A makeA() {
        return new A() {
            @Override
            public void f() {
                System.out.println("匿名内部类：public C.f()");
            }
            public void g() {
                System.out.println("匿名内部类：public C.g()");
            }
            void u() {
                System.out.println("匿名内部类：package C.u()");
            }
            protected void v() {
                System.out.println("匿名内部类：protected C.v()");
            }
            private void w() {
                System.out.println("匿名内部类：private C.w()");
            }
        };
    }
}
class AnonymousImplementation {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        A a = AnonymousA.makeA();
        a.f();
        System.out.println(a.getClass().getName()); // out: AnonymousA$1
        // 调用匿名内部类的全部方法：
        HiddenImplementation.callHiddenMethod(a, "g");
        HiddenImplementation.callHiddenMethod(a, "u");
        HiddenImplementation.callHiddenMethod(a, "v");
        HiddenImplementation.callHiddenMethod(a, "w");
    }
}

/**
 * 私有字段的测试：
 */
class WithPrivateFinalField {
    private int i = 47;
    private final int i2 = 477;
    private final String s = "一个 private final 的 s";
    private String s2 = "一个 private 的 s";

    @Override
    public String toString() {
        return "WithPrivateFinalField{" +
                "i=" + i +
                ", i2=" + i2 +
                ", s='" + s + '\'' +
                ", s2='" + s2 + '\'' +
                '}';
    }
}
class ModifyingPrivateFields {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        WithPrivateFinalField pf = new WithPrivateFinalField();
        System.out.println(pf);

        // 获取并修改私有字段：
        Field fI = pf.getClass().getDeclaredField("i");
        fI.setAccessible(true);
        System.out.println("f.getInt(pf) = " + fI.getInt(pf));
        fI.setInt(pf, 111);

        Field fI2 = pf.getClass().getDeclaredField("i2");
        fI2.setAccessible(true);
        System.out.println("fI2.getInt(pf) = " + fI2.getInt(pf));
        fI2.setInt(pf, 111);

        Field fS = pf.getClass().getDeclaredField("s");
        fS.setAccessible(true);
        System.out.println("fS.get(pf) = " + fS.get(pf));
        fS.set(pf, "private final 的 s 已经被我修改过了！哈哈哈哈");

        Field fS2 = pf.getClass().getDeclaredField("s2");
        fS2.setAccessible(true);
        System.out.println("fS2.get(pf) = " + fS2.get(pf));
        fS2.set(pf, "private 的 s 已经被我修改过了！哈哈哈哈");

        // 再次查看被修改的字段的值：
        System.out.println(pf);
    }
}
/*
测试结果：
看起来任何方式都没法阻止反射调用那些非公共访问权限的方法（包括在私有内部类、匿名内部类里面的）。
对于字段来说也是这样，即便是 private 字段。

有一个特殊的点，对于 final 字段：
在修改时，final 字段是安全的，运行时系统会在不抛出异常的情况下接受任何修改的尝试，
但是实际上不会发生任何修改。（就是可以编写修改的代码，但这对于 final 字段来说，没有什么效果！）
 */

/**
 * 【总结】
 * 通常，所有这些违反访问权限的操作并不是什么十恶不赦的。
 * 首先说明，不应该去调用这些标志为 private 或包访问权限的方法。
 * 如果有人使用反射调用了这写越权的方法，那么对他们（客户端程序员）来说，
 * 如果你修改了这些方法的某些地方，他们不应该抱怨（因为这些本来就不是属于他们去使用的）。
 * 另一方面，总在类中留下后门，也许会帮助你解决某些特定类型的问题（这些问题往往除此之外，别无它法）。
 * 总之，不可否认，反射给我们带来了很多好处。
 */


