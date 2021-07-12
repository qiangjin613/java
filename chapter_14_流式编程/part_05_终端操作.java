/*
在以下操作将会获取流的最终结果。
至此我们无法再继续往后传递流。
可以说，终端操作总是我们在流管道中所做的最后一件事。
 */

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/*
【数组】
1. toArray()：将流转换成适当类型的数组。
2. toArray(generator)：在特殊情况下，生成自定义类型的数组。
当我们需要得到数组类型的数据以便于后续操作时，上面的方法就很有用。

示例：
假设我们需要复用流产生的随机数。
 */
class RandInts {
    private static int[] rints = new Random(47).ints(0, 1000)
            .limit(100)
            .toArray();
    public static IntStream rands() {
        return Arrays.stream(rints);
    }
}
/*
上例将获取的随机数流保存在 rints 中。
这样一来，每次调用 rands() 的时候可以重复获取相同的整数流。
 */




/*
【循环】

 */














