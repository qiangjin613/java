import java.util.Arrays;

class MultidimensionalPrimitiveArray {
    public static void main(String[] args) {
        int[][] a = {
                {11, 12, 13, 14},
                {21, 22, 23, 24}
        };
        System.out.println(Arrays.deepToString(a));

        // [1] 不规则数组
        // [2] 元素默认初始化 0
        int[][][][] b = new int[10][2][3][4];
        System.out.println(Arrays.deepToString(b));
    }
}


/**
 * 使用 Arrays.deepToString() 打印对象数组：
 */
class Z {
    private String name;
    private String id;

    public Z(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Z{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
class MultiDimWrapperArray {
    public static void main(String[] args) {
        Z[][] zs =  {
                {new Z("xz", "001"), new Z("xzz", "002"), },
                {new Z("dz", "011"), },
        };
        System.out.println(Arrays.deepToString(zs));
    }
}



/*
tip：
    1. 使用 Arrays.deepToString() 可打印多维数组。
    （方法头：public static String deepToString(Object[] a)）
    2. 使用 Arrays.setAll() 可使用生成器来生成插入数组中的值
 */
