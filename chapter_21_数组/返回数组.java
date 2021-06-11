/*
在 C/C++ 中，无法返回一个数组，只能是返回一个指向数组的指针。
这会带来一些问题，因为对数组生存期的控制变得很混乱，这会导致内存泄露。

而在 Java 中，你只需返回数组，
你永远不用为数组担心，只要你需要它，它就可用，
垃圾收集器会在你用完后把它清理干净。
 */

import java.util.Arrays;
import java.util.Random;

class IceCreamFlavors {
    private static Random rand = new Random(47);
    public static int[] flavorSet(int n) {
        int[] ints = new int[n];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = rand.nextInt(100);
        }
        return ints;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(flavorSet(10)));
    }
}
