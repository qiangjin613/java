package _2_策略模式;

/*
使用 策略模式 改造。
> 策略模式（Strategy）定义了算法家族，分别分装起来，让算法之间可以互相替换。此模式让算法的变化不会影响到使用算法的客户。

商场收银时如何促销，其实都是一些算法的不同，用工厂来生成算法对象没有错，
但算法本身只是一种策略，最重要的是这些算法是随时都可能互相替换的，这就是 变化点！

封装变化点是面向对象的一种很重要的思维方式。（寻找“变化点” -> 封装“变化点”）
--- --- --- ---
在策略模式中，对于 V2 版本中的收银软件：
    CashSuper 就是抽象策略；
    正常收费 CashNormal、打折收费 CashRebate 和返利收费 CashReturn 就是三个具体的策略。
要改造成策略模式，只需要增加一个 CashContext 类，并改一下客户端就可以了。
 */

// 上下文
class CashContext {
    private CashSuper cs;

    public CashContext(CashSuper cs) {
        this.cs = cs;
    }

    public double getResult(double money) {
        return cs.acceptCash(money);
    }
}

// 客户端代码
class Sys_4 {
    /* 总价 */
    double total = 0.0d;

    private void method(double price, int num, int type) {
        CashContext cc = null;
        switch (type) {
            case 1: cc = new CashContext(new CashRebate(0.8)); break; /* 打八折 */
            case 2: cc = new CashContext(new CashRebate(0.75)); break; /* 打七五折 */
            case 3: cc = new CashContext(new CashRebate(0.5)); break; /* 打五折 */
            case 4: cc = new CashContext(new CashReturn(300, 100)); break; /* 满300减100 */
            default: cc = new CashContext(new CashNormal());
        }
        double totalPrice = cc.getResult(price) * num;
        total += totalPrice;
        System.out.println("单价：" + price + "\n数量：" + num + "\n合计：" + total);
    }
}

/*
在上述 策略方式 的使用中，发现回到了老路，在客户端去判断使用哪个算法。

进一步优化：将判断使用算法的过程从客户端移走。
使用 策略 + 工厂的组合模式。
如下：
 */

// 改造后的 CashContext：
class CashContext2 {
    private CashSuper cs;

    public CashContext2(int type) {
        switch (type) {
            case 1: cs = new CashRebate(0.8); break; /* 打八折 */
            case 2: cs = new CashRebate(0.75); break; /* 打七五折 */
            case 3: cs = new CashRebate(0.5); break; /* 打五折 */
            case 4: cs = new CashReturn(300, 100); break; /* 满300减100 */
            default: cs = new CashNormal();
        }
    }

    public double getResult(double money) {
        return cs.acceptCash(money);
    }
}
// 客户端代码：
class Sys_5 {
    /* 总价 */
    double total = 0.0d;

    private void method(double price, int num, int type) {
        CashContext2 cc = new CashContext2(type);
        double totalPrice = cc.getResult(price) * num;
        total += totalPrice;
        System.out.println("单价：" + price + "\n数量：" + num + "\n合计：" + total);
    }
}

/*
至此，收银软件的模式优化告一段落。

比较一下 策略模式和简单工厂模式在客户端使用上的区别：
- 简单工厂中，需要让客户端认识两个类：CashSuper、CashFactory；
- 而策略模式中与简单工厂的结合，只需让客户端认识：CashContext 就可以了。这使得具体的算法与客户端分离得更加彻底。

 */

