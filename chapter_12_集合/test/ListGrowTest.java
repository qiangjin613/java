package test;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * 对扩容机制的验证
 */
public class ListGrowTest {
    public static void main(String[] args) throws Exception {
        ArrayList defaultCapacity = new ArrayList();
        System.out.println("new ArrayList() 初始化长度：" + getElementData(defaultCapacity));
        defaultCapacity.add(1);
        System.out.println("new ArrayList().add() 后长度：" + getElementData(defaultCapacity));

        System.out.println("--- --- ---");
        ArrayList emptyCapacity = new ArrayList(0);
        System.out.println("new ArrayList(0) 初始化长度：" + getElementData(emptyCapacity));
        emptyCapacity.add(1);
        System.out.println("new ArrayList(0).add() 后长度：" + getElementData(emptyCapacity));
    }

    public static int getElementData(ArrayList arrayList) throws Exception {
        Class<? extends ArrayList> aClass = arrayList.getClass();
        Field field = aClass.getDeclaredField("elementData");
        field.setAccessible(true);
        Object[] o = (Object[]) field.get(arrayList);
        return o.length;
    }
}
/*
new ArrayList() 初始化长度：0
new ArrayList().add() 后长度：10
--- --- ---
new ArrayList(0) 初始化长度：0
new ArrayList(0).add() 后长度：1
 */

class Book {
    private Boolean sign;
    Book(Boolean sign) {
        this.sign = sign;
    }

    @Override
    protected void finalize() throws Throwable {
        if (sign) System.out.println("本书要被退还后再清楚");
    }
}

class TestBook {
    public static void main(String[] args) {
        new Book(true);
        System.gc();
    }
}

