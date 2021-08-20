import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

/*
【构建复杂模型】
泛型的一个重要好处是能够简单安全地创建复杂模型。
 */


/**
 * 示例1：轻松地创建一个元组列表
 */
// TODO

/**
 * 示例2：一个商店模型：具有过道、货架和产品
 */
class Product {
    private final int id;
    private String desc;
    private double price;

    Product(int idNumber, String descr, double price) {
        id = idNumber;
        desc = descr;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id: " + id +
                ", desc: " + desc +
                ", price: $" + price + '}';
    }

    public void priceChange(double change) {
        price += change;
    }

    public static Supplier<Product> generator = new Supplier<Product>() {
        private Random rand = new Random(47);
        @Override
        public Product get() {
            return new Product(rand.nextInt(1000),
                    "Test",
                    Math.round(rand.nextDouble() * 1000.0) + 0.99);
        }
    };
}

/**
 * 货架
 */
class Shelf extends ArrayList<Product> {
    Shelf(int nProcucts) {
        for (int i = 0; i < nProcucts; i++) {
            add(Product.generator.get());
        }
    }
}

/**
 * 过道
 */
class Aisle extends ArrayList<Shelf> {
    Aisle(int nShelves, int nProducts) {
        for (int i = 0; i < nShelves; i++) {
            add(new Shelf(nProducts));
        }
    }
}

/**
 * 检验台
 */
class CheckoutStand {}

/**
 * 办公室
 */
class Office {}

/**
 * 商店
 */
class Store extends ArrayList<Aisle> {
    private ArrayList<CheckoutStand> checkouts = new ArrayList<>();
    private Office office = new Office();

    public Store(int nAisles, int nShelves, int nProducts) {
        for (int i = 0; i < nAisles; i++) {
            add(new Aisle(nShelves, nProducts));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Aisle a : this) {
            for (Shelf s : a) {
                for (Product p : s) {
                    result.append(p).append("\n");
                }
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Store(5, 4, 3));
    }
}
/*
尽管有复杂的层次结构，但多层的集合仍然是类型安全的和可管理的。
令人印象深刻的是，组装这样的模型并不需要耗费过多精力。
 */
