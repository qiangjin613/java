package 策略设计模式;


/*
使用接口实现策略模式，解决 FirstDemo 中的窘境。
 */


interface Procesor2 {
    default String name() {
        return getClass().getSimpleName();
    }
    Object process(Object input);
}
