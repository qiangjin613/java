/*
当将内部类向上转型为其基类，尤其是转型为一个接口的时候，内部类就有了用武之地
 */

interface Destination {
    String readLabel();
}
interface Contents {
    int value();
}
class Parcel4 {
    private class PContents implements Contents {
        private int i = 11;
        @Override
        public int value() {
            return i;
        }
    }
    protected final class PDestination implements Destination {
        private String label;
        private PDestination(String whereTo) {
            label = whereTo;
        }
        @Override
        public String readLabel() {
            return label;
        }
    }
    public Destination destination(String s) {
        return new PDestination(s);
    }
    public Contents contents() {
        return new PContents();
    }
}
class TestParcel {
    public static void main(String[] args) {
        /* 但要直接使用内部类时：没有执行权限 */
        // Parcel4.PContents pContents = p.new PContents();

        // 只能通过内部类 + 向上转型来使用内部类：
        Parcel4 p = new Parcel4();
        Contents c = p.contents();
        c.value();

        Destination d = p.destination("TT");
        d.readLabel();
    }
}

/*
普通（非内部）类的访问权限不能被设置为 private、protected
但是，内部类的访问权限没有这种限制。

private 内部类给类的设计者提供了一种途径，
通过这种方式可以完全阻止任何依赖于类型的编码，并且完全隐藏了实现的细节。

此外，从客户端程序员的角度来看，
由于不能访问任何新增加的、原本不属于公共接口的方法，所以扩展接口是没有价值的。

这也给 Java 编译器提供了生成高效代码的机会。
 */
