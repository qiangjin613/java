import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/*
如果你不知道对象的确切类型，RTTI 会告诉你。
但是，有一个限制：必须在编译时知道类型，才能使用 RTTI 检测它，并对信息做一些有用的事情。
换句话说，编译器必须知道你使用的所有类。

起初，这看起来并没有那么大的限制，但是假设你引用了一个不在程序空间中的对象。
实际上，该对象的类在编译时甚至对程序都不可用。
也许你从磁盘文件或网络连接中获得了大量的字节，并被告知这些字节代表一个类。
由于这个类在编译器为你的程序生成代码后很长时间才会出现，你如何使用这样的类？

在传统编程环境中，这是一个牵强的场景。
但是，当我们进入一个更大的编程世界时，会有一些重要的情况发生：
    1.第一个是基于组件的编程，你可以在应用程序构建器集成开发环境中使用快速应用程序开发（RAD）构建项目。
    这是一种通过将表示组件的图标移动到窗体上来创建程序的可视化方法。
    然后，通过在编程时设置这些组件的一些值来配置这些组件。
    这种设计时配置要求任何组件都是可实例化的，它公开自己的部分，并且允许读取和修改其属性。
    此外，处理图形用户界面（GUI）事件的组件必须公开有关适当方法的信息，
    以便 IDE 可以帮助程序员覆写这些事件处理方法。
    **反射提供了检测可用方法并生成方法名称的机制。**

    2.在运行时发现类信息的另一个令人信服的动机是提供跨网络在远程平台上创建和执行对象的能力。
    这称为远程方法调用（RMI），它使 Java 程序的对象分布在许多机器上。
    这种分布有多种原因。如果你想加速一个计算密集型的任务，你可以把它分解成小块放到空闲的机器上。
    或者你可以将处理特定类型任务的代码放在特定的机器上，这样机器就成为描述这些操作的公共存储库，
    并且可以很容易地更改它以影响系统中的每个人。

类 Class 支持反射的概念，
java.lang.reflect 库中包含类 Field、Method 和 Constructor（每一个都实现了 Member 接口）。
这些类型的对象由 JVM 在运行时创建，以表示未知类中的对应成员。
然后，可以使用 Constructor 创建新对象，
get() 和 set() 方法读取和修改与 Field 对象关联的字段，invoke() 方法调用与 Method 对象关联的方法。
此外，还可以调用便利方法 getFields()、getMethods()、getConstructors() 等，以返回表示字段、方法和构造函数的对象数组。
因此，匿名对象的类信息可以在运行时完全确定，编译时不需要知道任何信息。

重要的是要意识到反射没有什么魔力。
当你使用反射与未知类型的对象交互时，JVM 将查看该对象，并看到它属于特定的类（就像普通的 RTTI）。
在对其执行任何操作之前，必须加载 Class 对象。
因此，该特定类型的 .class 文件必须在本地计算机上或通过网络对 JVM 仍然可用。
因此，RTTI 和反射的真正区别在于，使用 RTTI 时，编译器在编译时会打开并检查 .class 文件。
换句话说，你可以用“正常”的方式调用一个对象的所有方法。
通过反射，.class 文件在编译时不可用；它由运行时环境打开并检查。
---------------------


通常，你不会直接使用反射工具，但它们可以帮助你创建更多的动态代码。
反射是用来支持其他 Java 特性的，例如对象序列化（参见附录：对象序列化）。
但是，有时动态提取有关类的信息很有用。


（反射和 RTTI 都是从 class 对象开始）
 */

/**
 * 【类方法提取器】
 * 考虑一个方法提取器，展示一个类的所有方法，甚至包括定义在基类中方法
 */
class ShowMethods {
    private final static Pattern p = Pattern.compile("\\w+\\.");

    /*
    自动添加的无参构造函数授予与类相同的访问权。
    本例中，生成的将是 "ShowMethods() {}" 默认访问权的构造函数
     */

    public static void main(String[] args) {
        args = "ShowMethods ShowMethods".split(" ");

        int lines = 0;
        try {
            /* 第一步就是要获取 class 对象 */
            Class<?> c = Class.forName(args[0]);
            // getMethods()、getConstructors() 都只能获取到 public 修饰的
            Method[] methods = c.getMethods();
            Constructor<?>[] ctors = c.getConstructors();
            if (args.length == 1) {
                for (Method method : methods) {
                    System.out.println(p.matcher(method.toString())
                            .replaceAll("")
                    );
                }
                for (Constructor<?> ctor : ctors) {
                    System.out.println(p.matcher(ctor.toString())
                            .replaceAll(""));
                }
                lines = methods.length + ctors.length;
            } else {
                for (Method method : methods) {
                    if (method.toString().contains(args[1])) {
                        System.out.println(p.matcher(method.toString())
                                .replaceAll(""));
                        lines++;
                    }
                }
                for (Constructor<?> ctor : ctors) {
                    if (ctor.toString().contains(args[1])) {
                        System.out.println(p.matcher(ctor.toString())
                                .replaceAll(""));
                        lines++;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("No such class: " + e);
        }
    }
}
