package atunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {}


/*
不包含任何元素的注解称为标记注解（marker annotation），
自定义的 @Test 就是标记注解。
 */
