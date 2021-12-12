package _2_策略模式;

/*
收银软件 v1.0
设计模式：无
 */

class Sys_1 {
    /* 总价 */
    double total = 0.0d;

    private void method(double price, int num) {
        double totalPrice = price * num;
        total += totalPrice;
        System.out.println("单价：" + price + "\n数量：" + num + "\n合计：" + total);
    }
}

/*
新需求：商场添加打折、满减等活动。
更改方式：使用 switch 进行区分。
 */

class Sys_2 {
    /* 总价 */
    double total = 0.0d;

    private void method(double price, int num, int type) {
        double totalPrice = 0d;
        switch (type) {
            case 0: totalPrice = price * num; break;
            case 1: totalPrice = price * num * 0.8; break; /* 打八折 */
            case 2: totalPrice = price * num * 0.75; break; /* 打七五折 */
            case 3: totalPrice = price * num * 0.5; break; /* 打五折 */
        }
        total += totalPrice;
        System.out.println("单价：" + price + "\n数量：" + num + "\n合计：" + total);
    }
}
/*
对于以上改造，程序更加灵活了，要是有新的打折方式在 switch 中添加一个即可。
但是，如果促销方式变为 ”满减“ 活动，这时就需要新添一个方法进行处理。
 */
