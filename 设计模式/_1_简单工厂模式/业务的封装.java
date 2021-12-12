package _1_简单工厂模式;

/*
需求：实现一个计算器
目标：对业务进行封装，让业务逻辑与页面逻辑分开，让二者之间的耦合度下降。
原因：因为只有分开，才可以达到易维护或易扩展。
 */

import java.util.Scanner;

/**
 * 运算类
 */
class Operation {
    public static double GetResult(double numA, double numB, String opt) {
        double result = 0d;
        switch (opt) {
            case "+": result = numA + numB; break;
            case "-": result = numA - numB; break;
            case "*": result = numA * numB; break;
            case "/": result = numA / numB; break;
        }
        return result;
    }
}

/**
 * 客户端代码
 */
class Main {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.println("请输入 A、B 和操作符：");
            double numA = input.nextDouble();
            double numB = input.nextDouble();
            System.out.println(Operation.GetResult(numA, numB, "+"));
        } catch (Exception e) {
            System.out.println("输入有误：" + e.getMessage());
        }
    }
}
/*
使用了面向对象中的 封装，将业务代码封装到一个类中，让业务逻辑与界面逻辑之间的耦合度降低，达到易维护、易扩展的目标。
 */
