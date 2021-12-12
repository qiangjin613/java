package _2_策略模式;

/*
使用 简单工厂方式进行改造 收银软件。

针对于当前情况，如果对于所有的促销方式（打123456789折、满xx减xx、混合折扣、积xx分抵x元）都写一个子类，那也太多了！
观察后，将促销方式进行分类，然后使用参数进行控制具体值。
比如：”打折“活动，不管打几折，都只是折扣力度不同，具体的打折算法是一样的，这个促销方式可以写为一个类。

> 面向对象的编程，并不是类越多越好，类的划分是为了封装。但分类的基础是抽象，具有相同属性、功能的对象的抽象集和才是类。
 */

// 一个抽象的收费类
abstract class CashSuper {
    /**
     * 收费的方法
     * @param money 原价
     * @return 当前价格
     */
    public abstract double acceptCash(double money);
}

// 各种收费方式的子类
class CashNormal extends CashSuper {
    @Override
    public double acceptCash(double money) {
        return money; /* 正常收费 */
    }
}
class CashRebate extends CashSuper {
    private double moneyRebate = 1;

    public CashRebate(double moneyRebate) {
        this.moneyRebate = moneyRebate;
    }

    @Override
    public double acceptCash(double money) {
        return money * moneyRebate;
    }
}
class CashReturn extends CashSuper {
    private double moneyCondition = 0.0d;
    private double moneyReturn = 0.0d;

    public CashReturn(double moneyCondition, double moneyReturn) {
        this.moneyCondition = moneyCondition;
        this.moneyReturn = moneyReturn;
    }

    @Override
    public double acceptCash(double money) {
        double result = money;
        if (money >= moneyCondition) {
            result = money - money / moneyCondition * moneyReturn;
        }
        return result;
    }
}

// 收费工厂类
class CashFactory {
    public static CashSuper createCashAccept(int type) {
        CashSuper cs = null;
        switch (type) {
            case 0: cs = new CashNormal(); break;
            case 1: cs = new CashRebate(0.8); break; /* 打八折 */
            case 2: cs = new CashRebate(0.75); break; /* 打七五折 */
            case 3: cs = new CashRebate(0.5); break; /* 打五折 */
            case 4: cs = new CashReturn(300, 100); break; /* 满300减100 */
        }
        return cs;
    }
}

// 客户端部分
class Sys_3 {
    /* 总价 */
    double total = 0.0d;

    private void method(double price, int num, int type) {
        CashSuper cSuper = CashFactory.createCashAccept(type);
        double totalPrice = cSuper.acceptCash(price) * num;
        total += totalPrice;
        System.out.println("单价：" + price + "\n数量：" + num + "\n合计：" + total);
    }
}
/*
至此，在修改业务时，只需做简单的处理就可以。
但在商场促销这方面，可能是经常性地更改促销方式，每次维护都需要修改 活动类 和 工厂，还是比较麻烦。
 */
