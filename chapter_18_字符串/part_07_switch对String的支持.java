/*
到目前为止 switch 支持这样几种数据类型：
byte short int char String。

但是，作为一个程序员我们不仅要知道他有多么好用，还要知道它是如何实现的，
switch 对整型的支持是怎么实现的呢？对字符型是怎么实现的呢？ String 类型呢？

有一点 Java 开发经验的人这个时候都会猜测 switch 对 String 的支持
是使用 equals() 方法和 hashcode() 方法。
那么到底是不是这两个方法呢？
接下来我们就看一下，switch 到底是如何实现的。
 */

/**
 * 【switch 对整型支持的实现】
 */
class switchDemoInt {
    public static void main(String[] args) {
        int a = 5;
        switch (a) {
            case 1:
                System.out.println(1);
                break;
            case 5:
                System.out.println(5);
                break;
            default:
                break;
        }
    }
}
/*
switchDemoInt 生成的 class 文件如下：
--------
class switchDemoInt {
    switchDemoInt() {
    }

    public static void main(String[] args) {
        int a = 5;
        switch(a) {
        case 1:
            System.out.println(1);
            break;
        case 5:
            System.out.println(5);
        }
    }
}
-----------
反编译后的代码和之前的代码比较几乎没有任何区别，
那么我们就知道，switch对int的判断是直接比较整数的值。
 */


/**
 * 【switch 对字符型支持的实现】
 */
class switchDemoChar {
    public static void main(String[] args) {
        char a = 'b';
        switch (a) {
            case 'a':
                System.out.println(1);
                break;
            case 'b':
                System.out.println(5);
                break;
            default:
                break;
        }
    }
}
/*
switchDemoChar 生成的 class 文件如下：
--------
class switchDemoChar {
    switchDemoChar() {
    }

    public static void main(String[] args) {
        char a = 98;
        switch(a) {
        case 97: // 'a'
            System.out.println(1);
            break;
        case 98: // 'b'
            System.out.println(5);
        }
    }
}
----------
通过以上的代码作比较我们发现：
对 char 类型进行比较的时候，实际上比较的是 ascii 码，
编译器会把 char 型变量转换成对应的 int 型变量
 */


/**
 * 【switch 对字符串支持的实现】
 */
class switchDemoString {
    public static void main(String[] args) {
        String s = "Java";
        switch (s) {
            case "C++":
                System.out.println(1);
                break;
            case "Java":
                System.out.println(5);
                break;
            default:
                break;
        }
    }
}
/*
switchDemoString 生成的 class 文件如下：
-------
class switchDemoString {
    switchDemoString() {
    }

    public static void main(String[] args) {
        String s = "Java";
        byte var3 = -1;
        switch(s.hashCode()) {
        case 65763:
            if (s.equals("C++")) {
                var3 = 0;
            }
            break;
        case 2301506:
            if (s.equals("Java")) {
                var3 = 1;
            }
        }

        switch(var3) {
        case 0:
            System.out.println(1);
            break;
        case 1:
            System.out.println(5);
        }
    }
}
------------
看到这个代码，你知道原来字符串的switch是通过equals()和hashCode()方法来实现的。

记住，switch中只能使用整型，比如byte。short，char(ackii码是整型)以及int。

还好 hashCode() 方法返回的是int，而不是long。
通过这个很容易记住 hashCode 返回的是 int 这个事实。
仔细看下可以发现，
进行 switch 的实际是哈希值，然后通过使用 equals 方法比较进行安全检查，
这个检查是必要的，因为哈希可能会发生碰撞。
因此它的性能是不如使用枚举进行 switch 或者使用纯整数常量，
但这也不是很差。
因为 Java 编译器只增加了一个 equals 方法，
如果你比较的是字符串字面量的话会非常快，比如”abc” ==”abc”。
如果你把 hashCode() 方法的调用也考虑进来了，那么还会再多一次的调用开销，
因为字符串一旦创建了，它就会把哈希值缓存起来。
因此如果这个 switch 语句是用在一个循环里的，
比如逐项处理某个值，或者游戏引擎循环地渲染屏幕，这里 hashCode() 方法的调用开销其实不会很大。
 */

/*
【小结】
可以发现，其实 switch 只支持一种数据类型，那就是整型，
其他数据类型都是转换成整型之后再使用 switch 的。
 */
