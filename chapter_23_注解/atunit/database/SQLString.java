package atunit.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLString {
    int value() default 0;
    String name() default "";
    Constraints constraints() default @Constraints;
}

/*
这里利用了嵌套注解的功能，将数据库列的类型约束信息 @Constraints 嵌入其中。

constraints() 元素的默认值是 @Constraints。
由于在 @Constraints 注解类型之后，没有在括号中指明 @Constraints 元素的值，
因此，constraints() 的默认值为所有元素都为默认值的 @Constraints 注解。
如果要使得嵌入的 @Constraints 注解中的 unique() 元素为 true，
并作为 constraints() 元素的默认值，
可以像如下定义：
    Constraints constraints() default @Constraints(unique = true)
 */
