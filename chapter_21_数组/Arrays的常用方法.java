import java.util.Arrays;
import java.util.Collections;
import java.util.SplittableRandom;

/*
【java.util.Arrays】
1. 填充：fill()、setAll()、并行的 parallelSetAll()
2. 拷贝：copyOf()、copyOfRange()
3. 比较：equals()、deepEquals()
4. 排序：sort()、并行的 parallelSort()
5. 并行前缀：并行的 parallelPrefix()
 */

/**
 * 【使用 fill() 填充数组】 时间复杂度：O(n)
 * 其实现内部也是遍历数组
 */
class FillingArrays {
    public static void main(String[] args) {
        int size = 6;
        boolean[] a1 = new boolean[size];
        byte[] a2 = new byte[size];
        char[] a3 = new char[size];
        short[] a4 = new short[size];
        int[] a5 = new int[size];
        long[] a6 = new long[size];
        float[] a7 = new float[size];
        double[] a8 = new double[size];
        String[] a9 = new String[size];

        Arrays.fill(a1, true);
        Arrays.fill(a2, (byte)11);
        Arrays.fill(a3, 'x');
        Arrays.fill(a4, (short)17);
        Arrays.fill(a5, 19);
        Arrays.fill(a6, 23);
        Arrays.fill(a7, 29);
        Arrays.fill(a8, 47);
        Arrays.fill(a9, "Hello");
    }
}


/**
 * 【使用 setAll() 填充数组】 时间复杂度：O(n)
 * 其内部与 fill() 类似，不同的是 setAll() 使用生成器生成值
 */
class Bob {
    final int id;
    Bob(int n) {
        id = n;
    }
    @Override
    public String toString() {
        return "Bob{" +
                "id=" + id +
                '}';
    }
}
class SimpleSetAll {
    public static final int SZ = 8;
    static int val = 1;
    static char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static char getChar(int n) {
        return chars[n];
    }

    public static void main(String[] args) {
        int[] ia = new int[SZ];
        long[] la = new long[SZ];
        double[] da = new double[SZ];

        // [1] 使用 Lambda
        Arrays.setAll(ia, n -> n);
        Arrays.setAll(la, n -> n);
        Arrays.setAll(da, n -> n);

        Arrays.setAll(ia, n -> val++);
        Arrays.setAll(la, n -> val++);
        Arrays.setAll(da, n -> val++);

        // [2] 使用方法绑定
        Bob[] ba = new Bob[SZ];
        Arrays.setAll(ba, Bob::new);

        Character[] ca = new Character[SZ];
        Arrays.setAll(ca, SimpleSetAll::getChar);

        // [3] other
        /* 通过生成器函数修改现有的数组元素 */
        Arrays.setAll(la , n -> la[n] + 1);
    }
}


/**
 * 【使用 copyOf() 和 copyOfRange() 拷贝数组】
 * 与使用for循环手工执行复制相比，
 * copyOf() 和 copyOfRange() 复制数组要快得多。
 */
class Sup2 {
    private int id;
    Sup2(int n) {
        id = n;
    }
}
class Sub2 extends Sup2 {
    Sub2(int n) {
        super(n);
    }
}
class ArrayCopying {
    public static final int SZ = 15;

    public static void main(String[] args) {
        // [1] 基元数组
        int[] a1 = new int[SZ];
        Arrays.setAll(a1, new Count.Integer()::get);
        System.out.println(Arrays.toString(a1));

        /* 返回一个新的数组 */
        int[] a2 = Arrays.copyOf(a1, a1.length);
        System.out.println(Arrays.toString(a2));

        /* copy 短一点 */
        a2 = Arrays.copyOf(a1, a1.length / 2);
        System.out.println(Arrays.toString(a2));

        /* copy 长一点 */
        a2 = Arrays.copyOf(a1, a1.length + 5);
        System.out.println(Arrays.toString(a2));


        // [2] 包装类数组
        Integer[] a3 = new Integer[SZ];
        Arrays.setAll(a3, new Count.Integer()::get);
        System.out.println(Arrays.toString(a3));

        /* range: [from, to) */
        Integer[] a4 = Arrays.copyOfRange(a3, 4, 12);
        System.out.println(Arrays.toString(a4));

        // [3] 对象数组
        Sub2[] d = new Sub2[SZ / 2];
        Arrays.setAll(d, Sub2::new);
        System.out.println(Arrays.toString(d));

        /* 使用子类值拷贝给基类 */
        Sup2[] b = Arrays.copyOf(d, d.length, Sup2[].class);
        System.out.println(Arrays.toString(b));
        System.out.println(d + " " + b);


        /* 使用基类值拷给子类 */
        // [5] 因为这里只是向下转型，实质上还是相同的类型，所以没有问题
        Sub2[] d2 = Arrays.copyOf(b, b.length, Sub2[].class);
        System.out.println(Arrays.toString(d2));

        Sup2[] b2 = new Sup2[SZ / 2];
        Arrays.setAll(b2, Sup2::new);
        try {
            // [6] 这里的向下转型是可以进行编译的
            /*
                因为类型不兼容，得到一个运行时异常。
                因为派生对象中可能有基对象中没有的属性和方法。
            */
            Sub2[] d3 = Arrays.copyOf(b2, b2.length, Sub2[].class);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
/*
实例表明，原生数组和对象数组都可以被复制。
但是，如果复制对象的数组，虽然创建了一个新的数组，
但对于数组元素，只复制数组元素的引用—不复制数组元素对象本身。（浅拷贝）
 */


/*
【使用 equals() 比较一维数组，使用 deepEquals() 来比较多维数组】
对于所有原生类型和对象，这些方法都是重载的。

数组相等的含义：
1. 数组必须有相同数量的元素，
2. 并且每个元素必须与另一个数组中的对应元素相等，
3. 对每个元素使用 equals()
     （对于原生类型，使用原生类型的包装类的 equals() 方法;
     例如，int的Integer.equals()。）
 */
class ComparingArrays {
    public static final int SZ = 15;

    static String[][] twoDArray() {
        String[][] md = new String[5][];
        Arrays.setAll(md, n -> new String[n]);
        for (int i = 0, len = md.length; i < len; i++) {
            Arrays.setAll(md[i], n -> n + "");
        }
        return md;
    }

    public static void main(String[] args) {
        // 基元数组
        int[] a1 = new int[SZ], a2 = new int[SZ];
        Arrays.setAll(a1, new Count.Integer()::get);
        Arrays.setAll(a2, new Count.Integer()::get);
        System.out.println("a1 == a2: " + Arrays.equals(a1, a2));

        a2[3] = 11;
        System.out.println("a1 == a2: " + Arrays.equals(a1, a2));


        // 包装类数组
        Integer[] a1w = new Integer[SZ], a2w = new Integer[SZ];
        Arrays.setAll(a1w, new Count.Integer()::get);
        Arrays.setAll(a2w, new Count.Integer()::get);
        System.out.println("a1w == a2w: " + Arrays.equals(a1w, a2w));

        a2w[3] = 11;
        System.out.println("a1w == a2w: " + Arrays.equals(a1w, a2w));


        // 对象数组
        String[][] md1 = twoDArray(), md2 = twoDArray();
        System.out.println(Arrays.deepToString(md1));
        System.out.println("deepEquals(md1, md2): " + Arrays.deepEquals(md1, md2));
        System.out.println("md1 == md2: " + Arrays.equals(md1, md2));

        md1[4][1] = "#$#$#$#";
        System.out.println(Arrays.deepToString(md1));
        System.out.println("deepEquals(md1, md2): " + Arrays.deepEquals(md1, md2));
    }
}



/*
【使用 sort()、parallelSort() 排序】
Q：如何编写可复用的排序方法？
A：一个首要目标是“将易变的元素与稳定的元素分开”。
    在这里，保持不变的代码是一般的排序算法，但是变化的是对象的比较方式。
    因此，使用策略设计模式而不是将比较代码放入许多不同的排序源码中。
    使用策略模式时，变化的代码部分被封装在一个单独的类(策略对象)中。

sort() :
    原生类型的快速排序、对象的归并排序。
parallelSort() : （该算法需要不大于原始数组的额外工作空间）
    parallelSort() 算法将大数组拆分成更小的数组，
    直到数组大小达到极限，然后使用普通的 Arrays.sort() 方法。
    然后合并结果。
    （int MIN_ARRAY_SORT_GRAN = 1 << 13 = 8192）
 */
class CompType implements Comparable<CompType> {
    private static int count = 1;
    private static SplittableRandom r = new SplittableRandom(47);
    int i, j;

    public CompType(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public static CompType get() {
        return new CompType(r.nextInt(100), r.nextInt(100));
    }

    @Override
    public String toString() {
        String result = "CompType{" +
                "i=" + i +
                ", j=" + j +
                '}';
        if (count++ % 3 == 0) {
            result += "\n";
        }
        return result;
    }

    @Override
    public int compareTo(CompType ct) {
        return i < ct.i ? -1 : (i == ct.i ? 0 : 1);
    }

    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> get());
        System.out.println(Arrays.toString(a));
        Arrays.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
/*
sort() 将其参数转换为 Comparable类型。

如果没有实现 Comparable接口，
那么当试图调用 sort() 时，将在运行时获得一个 ClassCastException 。
 */


/**
 * 集合类包含一个方法 reverseOrder()，
 * 用来获得一个 Comparator（比较器） ，用来反转自然排序顺序。
 */
class Reverse {
    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> CompType.get());
        System.out.println(Arrays.toString(a));

        /* 正序排列 */
        Arrays.sort(a);
        System.out.println(Arrays.toString(a));

        /* 倒序排列 */
        Arrays.sort(a, Collections.reverseOrder());
        System.out.println(Arrays.toString(a));
    }
}


/**
 * 使用自己编写的比较器：
 */
class ComparatorTest {
    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> CompType.get());
        System.out.println(Arrays.toString(a));

        Arrays.sort(a, (o1, o2) ->
                (o1.j < o2.j ? -1 : (o1.j == o2.j ? 0 : 1)));
        System.out.println(Arrays.toString(a));
    }
}


/**
 * 并行排序
 */
class ParallelSort {
    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> CompType.get());
        Arrays.parallelSort(a);
        System.out.println(Arrays.toString(a));
    }
}


/**
 * binarySearch二分查找
 */
class ArraySearching {
    public static void main(String[] args) {
        Rand.Pint rand = new Rand.Pint();
        int[] a = new Rand.Pint().array(25);
        Arrays.sort(a);
        while (true) {
            int r = rand.getAsInt();
            int location = Arrays.binarySearch(a, r);
            if (location >= 0) {
                System.out.println("Location of " + r + " is " + location + ", a[" + location + "] is " + a[location]);
                break;
            }
        }
    }
}

/*
如果使用比较器对象数组进行排序，
那么在执行 binarySearch() 时必须包含相同的比较器。
 */
class AlphabeticSearch {
    public static void main(String[] args) {
        String[] sa = new Rand.String().array(30);
        Arrays.sort(sa, String.CASE_INSENSITIVE_ORDER);
        System.out.println(Arrays.toString(sa));

        int index = Arrays.binarySearch(sa, sa[10], String.CASE_INSENSITIVE_ORDER);
        System.out.println("Index: " + index + "\nData: " + sa[index]);
    }
}


/*
【parallelPrefix并行前缀】
类似与前缀和
 */
class ParallelPrefix1 {
    public static void main(String[] args) {
        int[] nums = new Count.Pint().array(11);
        System.out.println(Arrays.toString(nums));
        System.out.println("total = " + Arrays.stream(nums)
                .reduce(Integer::sum)
                .getAsInt());

        Arrays.parallelPrefix(nums, Integer::sum);
        System.out.println(Arrays.toString(nums));
        System.out.println(Arrays.stream(new Count.Pint().array(6))
                .reduce(Integer::sum)
                .getAsInt());
    }
}
/*
Stream.reduce()，只能得到最终结果，
而使用 Arrays.parallelPrefix()，还可以得到所有中间计算，以确保它们是有用的。
 */

class ParallelPrefix2 {
    public static void main(String[] args) {
        String[] strings = new Rand.String(1).array(8);
        System.out.println(Arrays.toString(strings));
        Arrays.parallelPrefix(strings, (a, b) -> a + b);
        System.out.println(Arrays.toString(strings));
    }
}

/*
使用流进行初始化非常优雅，
但是对于大型数组，这种方法可能会耗尽堆空间。

使用 setAll() 执行初始化更节省内存
 */

class ParallelPrefix3 {
    static final int SIZE = 10_000_000;

    public static void main(String[] args) {
        long[] nums = new long[SIZE];
        Arrays.setAll(nums, n -> n);
        Arrays.parallelPrefix(nums, Long::sum);
        System.out.println("First 20: " + nums[19]);
        System.out.println("First 200: " + nums[199]);
        System.out.println(nums.length - 1);
        System.out.println("All: " + nums[nums.length - 1]);
    }
}

/*
正确使用 parallelPrefix() 可能相当复杂，
所以通常应该只在存在内存或速度问题(或两者都有)时使用。
否则，Stream.reduce() 应该是您的首选。
 */
