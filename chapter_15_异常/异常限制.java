/**
 * 当覆盖方法的时候，只能抛出在基类方法的异常说明那里列出的那些异常
 */
class BaseballException extends Exception {}
class Strike extends BaseballException {}
class Foul extends BaseballException {}
class PopFoul extends Foul {}
abstract class Inning {
    Inning() throws BaseballException {}
    public void event() throws BaseballException {}
    public abstract void atBat() throws Strike, Foul;
    public void walk() {}
}
class StormException extends Exception {}
class RainedOut extends StormException {}
interface Storm {
    void event() throws RainedOut;
    void rainHard() throws RainedOut;
}
class StormyInning extends Inning implements Storm {
    // 异常限制对构造器不起作用，但是，基类构造器必须以这样或那样的方式被调用，所以派生狗在其的异常说明必须处理基构造函数异常:
    public StormyInning() throws RainedOut, BaseballException {}
    public StormyInning(String s) throws BaseballException {}

    // 重写的方法必须依照基类 walk() 方法，基类方法不会抛出 'PopFoul' 异常。通过强制派生类遵守基类方法的异常说明，对象的可替换性得到了保证
    // public void walk() throws PopFoul {} // 编译错误

    // 接口不能向现有的添加异常基类中的方法:
    // 与 'Inning' 中的 event() 冲突; 'Inning' 中的 event() 方法不会抛出 'RainedOut'
    // public void event() throws RainedOut {}
    // 与 'Storm' 中的 event() 冲突; 'Storm' 中的 event() 方法不会抛出 'BaseballException'
    // public void event() throws BaseballException {}
    // 可以不抛出任何异常。虽然 Inning.event() 抛出 BaseballException，Storm.event() 抛出 RainedOut，但两个异常并没有共同子类
    @Override
    public void event() {}

    // 覆盖的方法可以抛出继承的异常:
    @Override
    public void rainHard() throws RainedOut {}
    @Override
    public void atBat() throws PopFoul { }

    public static void main(String[] args) {
        // 类中的异常必须捕获该方法的基类版本:
        // 构造器抛出 RainedOut, BaseballException
        // si.atBat() 方法抛出 PopFoul
        try {
            StormyInning si = new StormyInning();
            si.atBat();
        } catch (PopFoul e) {
            System.out.println("PopFoul Exception");
        } catch (RainedOut e) {
            System.out.println("RainedOut Exception");
        } catch (BaseballException e) {
            System.out.println("BaseballException Exception");
        }

        // 如果向上转型的化:
        // 构造器抛出 RainedOut, BaseballException
        // i.atBat() 方法抛出 Strike, Foul
        try {
            Inning i = new StormyInning();
            i.atBat();
        } catch (Strike e) {
            System.out.println("Strike Exception");
        } catch (Foul rainedOut) {
            System.out.println("Foul Exception");
        } catch (RainedOut e) {
            System.out.println("RainedOut Exception");
        } catch (BaseballException rainedOut) {
            System.out.println("BaseballException Exception");
        }
    }
}

/**
 * 【异常限制与继承】
 * 尽管在继承过程中，编译器会对异常说明做出强制要求，但异常说明本身并不属于方法类型的一部分。（方法类型由方法名和参数类型组成）
 * 因此不能基于异常说明来重载方法。
 *
 * 此外，一个出现在基类方法的异常说明中的异常，不一定会出现在派生类方法的异常说明里。（异常说明范围变小了）
 * 这一点与继承的规则明显不同：
 * 在继承中，基类的方法必须出现在派生类里，换句话说，在继承和覆盖过程中，某个特定方法的 “异常说明的接口” 变小了。
 * 这恰好和类接口在继承时的情况相反！
 */