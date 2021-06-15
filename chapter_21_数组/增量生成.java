import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
【增量生成】
就是一个工具类，用于为不同类型生成增量值。
 */
interface Count {
    class Boolean implements Supplier<java.lang.Boolean> {
        private boolean b = true;

        @Override
        public java.lang.Boolean get() {
            b = !b;
            return b;
        }

        public java.lang.Boolean get(int n) {
            return get();
        }

        public java.lang.Boolean[] array(int sz) {
            java.lang.Boolean[] result = new java.lang.Boolean[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }
    class Pboolean {
        private boolean b = true;
        public boolean get() {
            b = !b;
            return b;
        }
        public boolean get(int n) {
            return get();
        }
        public boolean[] array(int sz) {
            boolean[] bs = new boolean[sz];
            for (int i = 0, len = bs.length; i < len; i++) {
                bs[i] = get();
            }
            return bs;
        }
    }


    class Integer implements Supplier<java.lang.Integer> {
        int i;

        @Override
        public java.lang.Integer get() {
            return i++;
        }

        public java.lang.Integer get(int n) {
            return get();
        }

        public java.lang.Integer[] array(int sz) {
            java.lang.Integer[] result = new java.lang.Integer[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }
    class Pint implements IntSupplier {
        int i;
        public int get() {
            return i++;
        }
        public int get(int n) {
            return get();
        }

        @Override
        public int getAsInt() {
            return get();
        }

        public int[] array(int sz) {
            int[] ints = new int[sz];
            Arrays.setAll(ints, n -> get());
            return ints;
        }
    }

    /*
    省略 byte、char、short、long、float、double 等
     */

    public static void main(String[] args) {
        Pboolean pb = new Pboolean();
        System.out.println(Arrays.toString(pb.array(10)));
    }
}


class TestCount {
    static final int SZ = 5;

    public static void main(String[] args) {
        System.out.println("Boolean");
        Boolean[] a1 = new Boolean[SZ];
        Arrays.setAll(a1, new Count.Boolean()::get);
        System.out.println(Arrays.toString(a1));

        a1 = Stream.generate(new Count.Boolean())
                .limit(SZ + 1)
                .toArray(Boolean[]::new);
        System.out.println(Arrays.toString(a1));

        a1 = new Count.Boolean().array(SZ + 6);
        System.out.println(Arrays.toString(a1));

        boolean[] a1b = new Count.Pboolean().array(SZ + 3);
        System.out.println(Arrays.toString(a1b));
    }
}
