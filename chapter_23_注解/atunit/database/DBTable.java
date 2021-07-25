package atunit.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
    String name() default "";
}

/*
在 @Target 注解中指定的每一个 ElementType 就是一个约束，
它告诉编译器，这个自定义的注解只能用于指定的类型。
你可以指定 enum ElementType 中的一个值，或者以逗号分割的形式指定多个值。
如果想要将注解应用于所有的 ElementType，那么可以省去 @Target 注解，但是这并不常见。
 */
