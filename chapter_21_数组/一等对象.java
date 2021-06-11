/*
对象数组和基本类型数组在使用上是完全相同的。

唯一的不同之处就是对象数组存储的是对象的引用，
而基元数组则直接存储基本数据类型的值。
 */

class ArrayOptions {
    public static void main(String[] args) {
        /* 对象数组，其引用默认被设置为 null */
        BerylliumSphere[] a = new BerylliumSphere[5];
        for (int i = 0; i < a.length; i++) {
            if (a[i] == null) {
                a[i] = new BerylliumSphere();
            }
        }
        ArrayShow.show(a);

        BerylliumSphere[] b = new BerylliumSphere[] {
                new BerylliumSphere(),
                new BerylliumSphere()
        };
        ArrayShow.show(b);

        BerylliumSphere[] c = {
                new BerylliumSphere(),
                new BerylliumSphere(),
                new BerylliumSphere()
        };
        ArrayShow.show(c);


        /* 基本类型数组，值默认被置为 0 */
        int[] ints = new int[10];
        ArrayShow.show(ints);
    }
}
