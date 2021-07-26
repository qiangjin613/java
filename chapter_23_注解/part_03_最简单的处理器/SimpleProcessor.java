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
@SupportedAnnotationTypes("atunit.Simple")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class SimpleProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement t : annotations) {
            System.out.println(t);
        }
        for (Element e : roundEnv.getElementsAnnotatedWith(Simple.class)) {
            display(e);
        }
        return false;
    }

    private void display(Element e) {
        System.out.println("=== " + e + " ===");
        System.out.println(e.getKind() +
                " : " + e.getModifiers() +
                " : " + e.getSimpleName() +
                " : " + e.asType());
        if (e.getKind().equals(ElementKind.CLASS)) {
            TypeElement te = (TypeElement) e;
            System.out.println(te.getQualifiedName());
            System.out.println(te.getSuperclass());
            System.out.println(te.getEnclosedElements());
        }
        if (e.getKind().equals(ElementKind.METHOD)) {
            ExecutableElement ex = (ExecutableElement) e;
            System.out.print(ex.getReturnType() + " ");
            System.out.print(ex.getSimpleName() + "(");
            System.out.print(ex.getParameters() + ")");
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
javac -processor annotations.simplest.SimpleProcessor SimpleTest.java
（不过这个命令，我没有成功执行过...）
 */
