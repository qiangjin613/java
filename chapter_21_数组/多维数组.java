import java.util.Arrays;

class MultidimensionalPrimitiveArray {
    public static void main(String[] args) {
        int[][] a = {
                {11, 12, 13, 14},
                {21, 22, 23, 24}
        };
        System.out.println(Arrays.deepToString(a));

        /* 不规则数组 */
        int[][][][] b = new int[10][2][3][4];
        System.out.println(Arrays.deepToString(b));
    }
}











