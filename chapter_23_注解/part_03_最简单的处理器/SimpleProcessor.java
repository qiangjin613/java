package part_03_最简单的处理器;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * 如下是一个十分简单的处理器，
 * 其所作的事情就是把 @Simple 注解相关的信息打印出来：
 */
@SupportedAnnotationTypes(
        "part_03_最简单的处理器.Simple")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SimpleProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for(TypeElement t : annotations)
            System.out.println(t);
        for(Element el : env.getElementsAnnotatedWith(Simple.class))
            display(el);
        return false;
    }
    private void display(Element el) {
        System.out.println("==== " + el + " ====");
        System.out.println(el.getKind() +
                " : " + el.getModifiers() +
                " : " + el.getSimpleName() +
                " : " + el.asType());
        if(el.getKind().equals(ElementKind.CLASS)) {
            TypeElement te = (TypeElement)el;
            System.out.println(te.getQualifiedName());
            System.out.println(te.getSuperclass());
            System.out.println(te.getEnclosedElements());
        }
        if(el.getKind().equals(ElementKind.METHOD)) {
            ExecutableElement ex = (ExecutableElement)el;
            System.out.print(ex.getReturnType() + " ");
            System.out.print(ex.getSimpleName() + "(");
            System.out.println(ex.getParameters() + ")");
        }
    }
}
/*
（旧的，失效的）apt 版本的处理器需要额外的方法来确定支持哪些注解以及支持的 Java 版本。
不过，现在可以简单的使用 @SupportedAnnotationTypes 和 @SupportedSourceVersion 注解
（这是一个很好的示例关于注解如何简化你的代码）。

在这个 APT（Annotation Processing Tool） 类中，唯一需要实现的方法就是 process()，这里是所有行为发生的地方，
第一个参数告诉你哪个注解是存在的，第二个参数保留了剩余信息。


另外，如果只是通过平常的方式来编译 SimpleTest.java，你不会得到任何结果。
为了得到注解输出，你必须增加一个 processor 标志并且连接注解处理器类：

javac -processor part_03_最简单的处理器.SimpleProcessor SimpleTest.java

Q：如果”若找不到类“的情况怎么处理呢？
使用小黑框执行 javac 命令时，若找不到类，则要对 CLASSPATH 进行配置，
因为 JVM 是通过 CLASSPATH 的路径和包的路径寻找（.class）文件，从而寻找到该类。

怎么确定 CLASSPATH 路径呢？
同过两步搞定：
1. 寻找 .class 文件的位置
SimpleProcessor.class 位于以下这个位置：
D:\qiang.jin\java-notes\out\production\java-notes\part_03_最简单的处理器
2.去掉 package，即为 CLASSPATH 路径：
D:\qiang.jin\java-notes\out\production\java-notes

Q：还有一个问题：“编码GBK的不可映射字符”
这是因为 package 名称使用了中文，或着代码中使用中文导致。
最简单的解决方法是将代码中的中文去掉就好了
 */
