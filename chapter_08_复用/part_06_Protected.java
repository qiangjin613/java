/**
 * protected: 对于任何继承它的子类或在同一包中的类，它是可访问的
 *
 * 较好的一种方式：
 *      将属性声明为 private 以一直保留更改底层实现的权利。
 *      然后通过 protected 控制类的继承者的访问权限。
 */

class Villain {
    private String name;
    protected void set(String name) {
        this.name = name;
    }

    Villain(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Villain{" +
                "name='" + name + '\'' +
                '}';
    }
}
class Orc extends Villain {
    private int orcNumber;

    public Orc(String name, int orcNumber) {
        super(name);
        this.orcNumber = orcNumber;
    }

    public void change(String name, int orcNumber) {
        set(name);
        this.orcNumber = orcNumber;
    }

    @Override
    public String toString() {
        return "Orc{" +
                "orcNumber=" + orcNumber +
                '}';
    }

    public static void main(String[] args) {
        Orc o = new Orc("Limburger", 12);
        System.out.println(o);
        o.change("Bpb", 19);
        System.out.println(o);
    }
}