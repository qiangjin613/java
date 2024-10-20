package reflection.xxx;

class C implements A {
    @Override
    public void f() {
        System.out.println("public f()");
    }

    public void g() {
        System.out.println("public g()");
    }

    void u() {
        System.out.println("package u()");
    }

    protected void v() {
        System.out.println("protected v()");
    }

    private void w() {
        System.out.println("private w()");
    }
}
