package part_04_更复杂的处理器;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 先定义一个 @ExtractInterface 注解，
 * 用于提取类中方法的注解，所以它可以被抽取成为一个接口：
 * （详情看 Multiplier 测试类 和 IfaceExtractorProcessor 处理器）
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface ExtractInterface {
    String interfaceName() default "-!!-";
}
/*
RetentionPolicy 的值为 SOURCE，
这是为了在提取类中的接口之后不再将注解信息保留在 class 文件中。
 */
