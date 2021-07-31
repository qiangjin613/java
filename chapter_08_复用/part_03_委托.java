/**
 * 委托介于继承和组合之间
 *
 * Java 是不支持委托的，但可以使用设计模式来实现
 */

/**
 * 宇宙飞船的例子：
 * 控制模块 SpaceShipControls
 */
class SpaceShipControls {
    void up(int velocity) {}
    void down(int velocity) {}
    void left(int velocity) {}
    void right(int velocity) {}
    void forward(int velocity) {}
    void back(int velocity) {}
    void turboBoost() {}
}

/**
 * 使用“继承”构建宇宙飞船
 */
class DerivedSpaceShip extends SpaceShipControls {
    private String name;
    public DerivedSpaceShip(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DerivedSpaceShip{" +
                "name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        DerivedSpaceShip protector = new DerivedSpaceShip("NSEA Protector");
        protector.forward(100);
    }
}

/**
 * 使用继承，一艘飞船包含了 SpaceShipControls，同时 SpaceShipControls 中的所有方法都暴露在宇宙飞船中。
 *
 * 使用委托解决了这个难题：
 *      方法被转发到底层 controls 对象，因此接口与继承的接口是相同的。
 *      但是对委托有更多的控制，因为可以选择只在成员对象中提供方法的子集。
 */
class SpaceShipDelegation {
    private String name;
    private SpaceShipControls controls = new SpaceShipControls();
    public SpaceShipDelegation(String name) {
        this.name = name;
    }

    // 委托方法：
    public void up(int velocity) {
        controls.up(velocity);
    }
    public void down(int velocity) {
        controls.down(velocity);
    }
    public void left(int velocity) {
        controls.left(velocity);
    }
    public void right(int velocity) {
        controls.right(velocity);
    }
    public void forward(int velocity) {
        controls.forward(velocity);
    }
    public void back(int velocity) {
        controls.back(velocity);
    }
    public void turboBoost() {
        controls.turboBoost();
    }

    public static void main(String[] args) {
        SpaceShipDelegation protector = new SpaceShipDelegation("NSEA Protector");
        protector.forward(100);
    }
}
/*
也可以使用“半委托”实现飞船的组装。


只需将成员对象 SpaceShipControls 声明为 public 即可（“半委托”的一种）。
有时让类的用户直接访问到新类中的组合成分是有意义的。
成员对象隐藏了具体实现，所以这是安全的。
当用户知道你正在组装一组部件时，会使得接口更加容易理解。
（但是，这这只是一个特例。通常来说，属性还是应该声明为 private）
 */


/**
 * 半委托例子：
 */
class SpaceShipDelegation2 {
    private String name;
    public SpaceShipControls controls = new SpaceShipControls();
    public SpaceShipDelegation2(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        SpaceShipDelegation2 protector = new SpaceShipDelegation2("NSEA Protector");
        /*
        在这里明确指出了 forward() 方法是哪个类的对象调用的。（半委托的优点）
         */
        protector.controls.forward(100);
    }
}
