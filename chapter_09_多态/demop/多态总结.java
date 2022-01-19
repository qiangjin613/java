package demop;

class Bath {
    // [1] 定义时初始化
    private String s1 = "happy";
    private String s2 = f();
    private String s3, s4;
    private int i;
    private float toy;

    public Bath() {
        System.out.println("Inside Bath()");
        // [2] 构造函数中初始化
        s3 = "Joy";
        toy = 3.14f;
    }
    // [4] 使用实例（初始化块）初始化
    {
        i = 47;
    }

    @Override
    public String toString() {
        if (s4 == null) {
            // [3] 实际使用对象之前
            s4 = "Joy";
        }
        return "Bath{" +
                "s1='" + s1 + '\'' +
                ", s2='" + s2 + '\'' +
                ", s3='" + s3 + '\'' +
                ", s4='" + s4 + '\'' +
                ", i=" + i +
                ", toy=" + toy +
                '}';
    }

    private String f() {
        return "Hello World, My World!";
    }
}