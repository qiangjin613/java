package part_01_private;

interface Resource {
    int getValue();
    void setValue(int x);
}

/**
 * 在下面的 Singleton 类中，private 在内部类似乎并不是那么严格
 */
class Singleton {
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
        private static Resource resource = new Singleton.ResourceImpl(47);
    }

    public static Resource getResource() {
        return Singleton.ResourceHolder.resource;
    }
}
