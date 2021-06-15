import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

/*
【随机生成】
是一个工具类，用于为不同类型生成随机值。
 */
interface Rand {
    int MOD = 10_000;
    class Boolean implements Supplier<java.lang.Boolean> {
        SplittableRandom r = new SplittableRandom(47);
        @Override
        public java.lang.Boolean get() {
            return r.nextBoolean();
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
        SplittableRandom r = new SplittableRandom(47);
        public boolean get() {
            return r.nextBoolean();
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
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Integer get() {
            return r.nextInt(MOD);
        }
        public java.lang.Integer get(int n) {
            return get();
        }
        public java.lang.Integer[] array(int sz) {
            int[] primitive = new Pint().array(sz);
            java.lang.Integer[] result = new java.lang.Integer[sz];
            for(int i = 0; i < sz; i++)
                result[i] = primitive[i];
            return result;
        }
    }
    class Pint implements IntSupplier {
        SplittableRandom r = new SplittableRandom(47);
        @Override
        public int getAsInt() {
            return r.nextInt(MOD);
        }
        public int get(int n) { return getAsInt(); }
        public int[] array(int sz) {
            return r.ints(sz, 0, MOD).toArray();
        }
    }

    /*
    省略了 byte、char、short、int、long、float、double 等
     */

    class String
            implements Supplier<java.lang.String> {
        SplittableRandom r = new SplittableRandom(47);
        private int strlen = 7; // Default length
        public String() {}
        public String(int strLength) {
            strlen = strLength;
        }
        @Override
        public java.lang.String get() {
            return r.ints(strlen, 'a', 'z' + 1)
                    .collect(StringBuilder::new,
                            StringBuilder::appendCodePoint,
                            StringBuilder::append).toString();
        }
        public java.lang.String get(int n) {
            return get();
        }
        public java.lang.String[] array(int sz) {
            java.lang.String[] result =
                    new java.lang.String[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }
}


class TestRand {
    static final int SZ = 5;

    public static void main(String[] args) {
        System.out.println("Boolean");
        Boolean[] a1 = new Boolean[SZ];
        Arrays.setAll(a1, new Rand.Boolean()::get);
        System.out.println(Arrays.toString(a1));

        a1 = Stream.generate(new Rand.Boolean())
                .limit(SZ + 1)
                .toArray(Boolean[]::new);
        System.out.println(Arrays.toString(a1));

        a1 = new Rand.Boolean().array(SZ + 6);
        System.out.println(Arrays.toString(a1));

        boolean[] a1b = new Rand.Pboolean().array(SZ + 3);
        System.out.println(Arrays.toString(a1b));
    }
}
