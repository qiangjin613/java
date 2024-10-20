package reflection;

import java.lang.reflect.Field;

public class ModifyingPrivateFields {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        ModifyingPrivateField pf = new ModifyingPrivateField();
        System.out.println(pf);

        Class<? extends ModifyingPrivateField> cls = pf.getClass();

        Field i = cls.getDeclaredField("i");
        i.setAccessible(true);
        System.out.println(i.getInt(pf));
        i.setInt(pf, 47);
        System.out.println(pf);

        System.out.println();

        Field s = cls.getDeclaredField("s");
        s.setAccessible(true);
        System.out.println(s.get(pf));
        s.set(pf, "[Modify]private final String s");
        System.out.println(pf);

        System.out.println();

        Field s2 = cls.getDeclaredField("s2");
        s2.setAccessible(true);
        System.out.println(s2.get(pf));
        s2.set(pf, "[Modify]private final String s2");
        System.out.println(pf);
    }
}

class ModifyingPrivateField {
    private int i = 1; // 可以被反射修改
    private final String s = "private final String s"; // 不可以被反射修改
    private String s2 = "private String s2"; // 可以被反射修改

    @Override
    public String toString() {
        return "ModifyingPrivateField{" +
                "i=" + i +
                ", s='" + s + '\'' +
                ", s2='" + s2 + '\'' +
                '}';
    }
}