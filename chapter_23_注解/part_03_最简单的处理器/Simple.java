package part_03_最简单的处理器;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD,
        ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE,
        ElementType.PACKAGE, ElementType.FIELD,
        ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface Simple {
    String value() default "-default-";
}
/*
@Retention 的参数为 SOURCE，意味着注解会再存留在编译后的代码中。
    这在编译时处理注解是没有必要的，它只是指出，
    在这里，javac 是唯一有机会处理注解的代理。
@Target 声明了几乎所有的目标类型（除了 PACKAGE）。
 */
