/*
也许单例模式是最简单的设计模式，
它是一种提供一个且只有一个对象实例的方法。
 */

interface Resource {
    int getValue();
    void setValue(int x);
}

/*
 * 由于这不是从Cloneable基类继承而且没有添加可克隆性，
 * 因此将其设置为final可防止通过继承添加可克隆性。
 * 这也实现了线程安全的延迟初始化：
 */
final class Singleton {

    /* 防止客户端程序员直接创建对象 */
    private Singleton() {}

    private static final class ResourceImpl implements Resource {
        private int i;

        private ResourceImpl(int i) {
            this.i = i;
        }

        @Override
        public int getValue() {
            return i;
        }

        @Override
        public void setValue(int x) {
            i = x;
        }
    }

    private static class ResourceHolder {
        private static Resource resource = new ResourceImpl(47);
    }

    public static Resource getResource() {
        return ResourceHolder.resource;
    }
}

class SingletonPattern {
    public static void main(String[] args) {
        Resource r = Singleton.getResource();
        System.out.println(r.getValue());

        Resource r2 = Singleton.getResource();
        r2.setValue(9);

        System.out.println(r.getValue());
        try {
            // Singleton r3 = (Singleton) r2.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
/*
创建单例的关键是防止客户端程序员直接创建对象。
在这里，这是通过在 Singleton 类中将 Resource 的实现作为私有类来实现的。

此时，你将决定如何创建对象。
在这里，它是按需创建的，在第一次访问的时候创建。
该对象是私有的，只能通过 public getResource() 方法访问。

懒惰地创建对象的原因是它嵌套的私有类 resourceHolder 在首次引用之前不会加载（在getResource（）中）。
当 Resource 对象加载的时候，静态初始化块将被调用。
由于 JVM 的工作方式，这种静态初始化是线程安全的。为保证线程安全，Resource 中的 getter/setter 是同步的。
 */
