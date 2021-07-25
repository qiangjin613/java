package atunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    int id();
    String description() default "no description";
}

/*
@UseCase 注解中，
id 和 description 为注解元素；
他们对应的注解类型分别为：int 和 String。

所有可用的注解元素类型如下：
1. 所有基本类型（int、float、boolean等）
2. String
3. Class
4. enum
5. Annotation
6. 以上类型的数组

如果使用了其他类型，编译器就会报错。
注意，也不允许使用任何包装类型，但是由于自动装箱的存在，这不算是什么限制。
注解也可以作为元素的类型（注解嵌套是一个非常有用的技巧）。
 */
