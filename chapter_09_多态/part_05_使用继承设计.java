/*
虽然多态非常巧妙（使用继承），但会给设计带来负担。
事实上，如果利用已有类创建新类首先选择继承的话，事情会变得莫名的复杂。

【更好的方法是首先选择组合，特别是不知道该使用哪种方法时】
组合的好处：
    组合不会强制设计是继承层次结构，而且组合更加灵活，
    因为可以动态地选择类型（因而选择相应的行为），
    而继承要求必须在编译时知道确切类型。（无法在运行时才决定继承不同的对象，那再编译时就完全决定好了）
 */

/**
 * 使用继承，无法在运行时才决定继承不同的对象（那在编译时就完全决定好了）
 */
class Actor {
    public void act() {}
}
class HappyActor extends Actor {
    @Override
    public void act() {
        System.out.println("HappyActor");
    }
}
class SadActor extends Actor {
    @Override
    public void act() {
        System.out.println("SadActor");
    }
}
/**
 * 使用组合，在运行时使自己的状态发生变化（行为也就发生了相应的改变）
 * （这里使用了状态模式，在运行的时候动态改变 Actor 的对象）
 */
class Stage {
    // 使用多态
    private Actor actor = new HappyActor();
    public void change() {
        actor = new SadActor();
    }
    public void performPlay() {
        actor.act();
    }
}
class Transmogrify {
    public static void main(String[] args) {
        Stage stage = new Stage();
        stage.performPlay();
        stage.change();
        stage.performPlay();
    }
}

/**
 * actor 可以在运行时通过 change() 方法与其他不同的对象绑定，
 * performPlay() 的行为随之改变。
 * 这样，就获得了运行时的动态灵活性（称为状态模式）。
 *
 * 与之相反，无法在运行时才决定继承不同的对象（因为在编译时就完全决定好了）
 */


/**
 * 【向下转型与运行时类型信息】
 * 向上转型（在继承层次中向上移动）会丢失具体的类型信息，
 * 为了重新获取类型信息，就需要在继承层次中向下移动，使用向下转型。
 *
 * Q：向下转型时，如何知道一个形状类是圆、三角形正方形或其他一些类型？
 * A：为了解决这个问题，必须有某中方法确保向下转型是正确的（RTTI），防止意外转型到一个错误类型，
 *    进而发送对象无法接收的消息。这么做是不安全的。
 *
 *    Java 中，每次转型（包括向上和向下）都会被检查！
 *    所以即使只是进行一次普通的加括号形式的类型转换，
 *    在运行时这个转换仍会被检查，以确保它的确是希望的那种类型。
 *    如果不是，就会得到 ClassCastException （类转型异常）。
 *    这种在运行时检查类型的行为称作运行时类型信息（RTTI）。
 */
