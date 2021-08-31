import java.io.Serializable;

/*
【对在普通类的继承关系中的接口探索】
 */
interface TestA {
    void f();
}

class TestAImpl implements TestA {
    @Override
    public void f() {}
}

class TestSubA extends TestAImpl implements Serializable {
    public static void main(String[] args) {
        Class<TestSubA> c = TestSubA.class;
        Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> cInterface : interfaces){
            System.out.println(cInterface.getSimpleName());
        }
    }
}
/* Output:
Serializable

可见，对于接口是没有继承父类的一说的。
不过，父类继承了接口，如果父类是普通类，那么就会实现接口中的的方法，
到了子类也就有了接口中的方法。
 */
