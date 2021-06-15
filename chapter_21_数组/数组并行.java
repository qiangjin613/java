import java.util.Arrays;
import java.util.stream.LongStream;

/*
先来看一个 流式编程 的例子：
    创建一个数值由从零开始填充的长数组
 */
class CountUpward {
    static long[] fillCounted(int sz) {
        return LongStream.iterate(0, i -> i + 1)
                .limit(sz)
                .toArray();
    }

    public static void main(String[] args) {
        long[] l1 = fillCounted(20);
        System.out.println(Arrays.toString(l1));

        // 如果是这样，将会...   OutOfMemoryError（内存溢出）
        long[] l2 = fillCounted(Integer.MAX_VALUE);
    }
}
/*
可以看到，对于常规的 setAll() 是有效的，
但是初始化更大的数组，时间成为一个问题。

如果速度成为一个问题，
Arrays.parallelSetAll() 将(可能)更快地执行初始化。
 */


/**
 * 使用 Arrays.parallelSetAll()：
 */
class ParallelSetAll {
    static final int SZ = Integer.MAX_VALUE;
    static void intArray() {
        int[] ia = new int[10_000_000];
        Arrays.setAll(ia, new Rand.Pint()::get);
        /* 并行的 parallelSetAll() 比 setAll() 要快好多 */
        Arrays.parallelSetAll(ia, new Rand.Pint()::get);
    }

    public static void main(String[] args) {
        intArray();
    }
}
