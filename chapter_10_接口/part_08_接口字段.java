/*
接口中的字段都自动是 static 和 final 的，
所以，接口就成为了创建一组常量的方便的工具。

【这是在 Java 5 之前没有枚举类型的时候使用的常用习惯用法（现在还是使用枚举比较好）】
 */

import java.util.Random;

/**
 * Java 5 之前常量的定义方式就是使用接口来实现。
 */
interface Months {
    int JANUARY = 1, FEBRUARY = 2, MARCH = 3,
            APRIL = 4, MAY = 5, JUNE = 6, JULY = 7,
            AUGUST = 8, SEPTEMBER = 9, OCTOBER = 10,
            NOVEMBER = 11, DECEMBER = 12;
}

/*
自 Java 5 开始，
我们有了更加强大和灵活的关键字 enum，
那么在接口中定义常量组就显得没什么意义了。
 */


/*
【接口中的字段】
接口中定义的字段不能是“空 final"，
但是可以用非常量表达式初始化。
 */

interface RndVals {
    Random RAND = new Random(47);
    int RANDOM_INT = RAND.nextInt();
    long RANDOM_LONG = RAND.nextLong() * 10;
    double RANDOM_DOUBLE = RAND.nextDouble() * 10;
}

class TestRandVals {
    public static void main(String[] args) {
        System.out.println(RndVals.RANDOM_INT);
        System.out.println(RndVals.RANDOM_LONG);
        System.out.println(RndVals.RANDOM_DOUBLE);
    }
}

/*
这些字段不是接口的一部分，它们的值被存储在接口的静态存储区域中。
 */
