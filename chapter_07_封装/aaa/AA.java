package aaa;

public class AA {
    AA() {
        new A();
        new A(1);
        new A(1,2);
    }

    public static void main(String[] args) {
        new A();
        new A(1);
        A a = new A(1, 2);
        a.publicObjMethod();
        a.protectedObjMethod();
        a.defaultObjMethod();

        A.publicClassMethod();
        A.protectedClassMethod();
        A.defaultClassMethod();
    }
}
