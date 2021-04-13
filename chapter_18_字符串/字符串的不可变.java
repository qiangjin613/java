/**
 * String 对象是不可变的！
 * String 类中每一个看起来会修改 String 值的方法，实际上都是创建了一个全新的 String 对象，
 * 以包含修改后的字符串内容。而最初的 String 对象则丝毫未动。
 */
class Immutable {
    public static String upcase(String s) {
        return s.toUpperCase();
    }
    public static void main(String[] args) {
        String q = "howdy";
        System.out.println(q);
        String qq = upcase(q);
        // 每当把 String 对象作为方法的参数时，都会复制一份引用，
        // 而原始的引用指向的对象一直在单一的物理位置上，从未动过
        System.out.println(qq);
        System.out.println(q);
    }
}

/**
 * Q：为什么 String 的方法不会改变其参数？
 * A：对于一个方法而言，参数是为方法提供信息的，而不是想让该方法改变自己的。
 *
 * Q：给 String 起别名会影响间接影响字符串本身吗？
 * A：可以给一个 String 对象添加任意多的别名，
 *    但 String 对象是不可变的（是只读的），
 *    所以指向它的任何引用都不能改变它的值，String 的方法也不会修改其参数。
 *    因此，也就不会影响到字符串本身。
 */