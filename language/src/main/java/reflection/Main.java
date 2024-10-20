package reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    /**
     * 可重写方法成为 abstract，让其子类（派生类）强制实现该方法
     */
//    @Override
//    public  String toString();
}


class Test extends Main {

    private Test() {
        System.out.println("私有构造器被调用");
    }

    public static void main(String[] args) {

        Main m = new Main();
        Test t = new Test();

        System.out.println(Main.class.isInstance(t)); // true
        System.out.println(Test.class.isInstance(m)); // false
        System.out.println(Main.class.isAssignableFrom(Test.class)); // true
        System.out.println(Test.class.isAssignableFrom(Main.class)); // false

        List list = new ArrayList();
        Class<? extends List> aClass = list.getClass();
        System.out.println(aClass.getName());
        System.out.println(aClass.getSimpleName());
        System.out.println(aClass.getTypeName());
        System.out.println(aClass.getCanonicalName());

        System.out.println(Arrays.toString(aClass.getInterfaces()));
        System.out.println(Arrays.toString(aClass.getGenericInterfaces()));
        System.out.println(Arrays.toString(aClass.getAnnotatedInterfaces()));

        Test main = new Test();

        Class<ArrayList> arrayListClass = ArrayList.class;
        System.out.println(arrayListClass.isInstance(main));
        System.out.println(arrayListClass.isAssignableFrom(List.class));


        Class<Test> testClass = Test.class;

        Class<? super Test> superclass = testClass.getSuperclass();
//        try {
//            System.out.println(superclass.newInstance());
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }

        for (Constructor<?> constructor : testClass.getDeclaredConstructors()) {
            constructor.setAccessible(true);
            try {
                Object o = constructor.newInstance();
                System.out.println(o);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        }



        try {
            List list1 = aClass.newInstance();
            Class<ArrayList> arrayListClass11 = ArrayList.class;
            System.out.println(list1);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        return null;
    }
}