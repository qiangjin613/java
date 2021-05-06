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
        new A(1,2);

        A a = A.makeA3Args();
    }
}
