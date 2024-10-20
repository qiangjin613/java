package reflection.proxy;

public class RealObject implements Interface {

    @Override
    public void doSomething() {
        System.out.println("RealObject.doSomething() 执行");
    }
}
