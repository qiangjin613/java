import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 【类方法提取器】
 * 使用反射展示一个类的所有方法，甚至包括定义在基类中方法
 */
class ShowMethods {
    private static Pattern p = Pattern.compile("\\w+\\.");

    /*
    自动添加的无参构造函数授予与类相同的访问权。
    本例中，生成的将是 "ShowMethods() {}" 默认访问权的构造函数
     */

    public static void main(String[] args) {
        args = "ShowMethods ShowMethods".split(" ");

        int lines = 0;
        try {
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
                for (Constructor ctor : ctors) {
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
