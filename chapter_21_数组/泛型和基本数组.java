/*
之前，泛型不能和基元一起工作。
在这种情况下，我们必须进行必要的转换：基元数组 <=> 包装类型的数组

提供一个转换器：
 */
interface ConvertTo {
    // [1] 包装类型的数组 => 基元数组
    static boolean[] primitive(Boolean[] in) {
        int len = in.length;
        boolean[] result = new boolean[len];
        for (int i = 0; i < len; i++) {
            result[i] = in[i];
        }
        return result;
    }
    static char[] primitive(Character[] in) {
        int len = in.length;
        char[] result = new char[len];
        for (int i = 0; i < len; i++) {
            result[i] = in[i];
        }
        return result;
    }
    /*
    ...
     */

    // [2] 基元数组 => 包装类型的数组
    static Boolean[] boxed(boolean[] in) {
        int len = in.length;
        Boolean[] result = new Boolean[len];
        for (int i = 0; i < len; i++) {
            result[i] = in[i];
        }
        return result;
    }
    /*
    ...
     */
}
