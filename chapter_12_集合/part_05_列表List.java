import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
List 承诺将元素保存在特定的序列中，主要有两种类型的 List：
1. ArrayList
    擅长随机访问元素
2. LinkedList
    擅长插入和删除操作，提供优化的顺序访问
 */
class ListFeatures {
    public static void main(String[] args) {
        Random rand = new Random(47);

        List<Pet> pets = Pets.list(7);
        System.out.println(pets);

        List<Pet> sub = Arrays.asList(pets.get(2), pets.get(4), pets.get(6));

    }
}



















