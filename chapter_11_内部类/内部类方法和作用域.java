/*
局部内部类示例：
 */
class Parcel5 {
    public Destination destination(String s) {
        /* 在方法的作用域内创建一个完整的类 */
        final class PDestination implements Destination {
            private String label;
            private PDestination(String whereTo) {
                label = whereTo;
            }
            @Override
            public String readLabel() {
                return label;
            }
        }
        return new PDestination(s);
    }

    public static void main(String[] args) {
        Parcel5 p = new Parcel5();
        Destination dd = p.destination("TT");
    }
}

class Parcel6 {
    private void internalTracking(boolean b) {
        if (b) {
            /* 在方法内部的类 */
            class TrackingSlip {
                private String id;
                TrackingSlip(String s) {
                    id = s;
                }
                String getSlip() {
                    return id;
                }
                TrackingSlip ts = new TrackingSlip("slip");
                String s = ts.getSlip();
            }
        }
        /* 在 if 的外部是没有办法使用的 */
        // TrackingSlip x = new TrackingSlip("x");
    }
}

/*
像这种局部内部类的作用域，都是在该处范围内可用，在范围外是无法使用的，
所以对于局部内部类而言，使用 权限修饰符（private等）是没有意义的，编译器也是不允许的。
 */
