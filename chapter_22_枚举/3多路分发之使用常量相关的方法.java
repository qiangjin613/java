/*
常量相关的方法允许我们为每个 enum 实例提供方法的不同实现，
这使得常量相关的方法似乎是实现多路分发的完美解决方案。

不过，通过这种方式，enum 实例虽然可以具有不同的行为，
但它们仍然不是类型，
不能将其作为方法签名中的参数类型来使用。

最好的办法是将 enum 用在 switch 语句中.
 */

/**
 * 如下：
 */
enum RoShamBo3 implements Competitor<RoShamBo3> {
    PAPER {
        @Override
        public OutCome compete(RoShamBo3 it) {
            switch (it) {
                default:
                case PAPER:
                    return OutCome.DRAW;
                case SCISSORS:
                    return OutCome.LOSE;
                case ROCK:
                    return OutCome.WIN;
            }
        }
    },
    SCISSORS {
        @Override
        public OutCome compete(RoShamBo3 it) {
            switch (it) {
                default:
                case PAPER:
                    return OutCome.LOSE;
                case SCISSORS:
                    return OutCome.DRAW;
                case ROCK:
                    return OutCome.WIN;
            }
        }
    },
    ROCK {
        @Override
        public OutCome compete(RoShamBo3 it) {
            switch (it) {
                default:
                case PAPER:
                    return OutCome.LOSE;
                case SCISSORS:
                    return OutCome.WIN;
                case ROCK:
                    return OutCome.DRAW;
            }
        }
    };

    @Override
    public abstract OutCome compete(RoShamBo3 it);

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            RoShamBo3 a = Enums.random(RoShamBo3.class);
            RoShamBo3 b = Enums.random(RoShamBo3.class);
            System.out.println(a + " vs. "  + b + ": " + a.compete(b));
        }
    }
}
/*
虽然这种方式可以工作，但是却不甚合理，
如果采用 RoShamBo2.java 的解决方案，
那么在添加一个新的类型时，只需更少的代码，而且也更直接。
 */

/**
 * 将上述代码简化一下：
 */
enum RoShamBo4 implements Competitor<RoShamBo4> {
    ROCK {
        @Override
        public OutCome compete(RoShamBo4 opponent) {
            return compete(SCISSORS, opponent);
        }
    },
    SCISSORS {
        @Override
        public OutCome compete(RoShamBo4 opponent) {
            return compete(PAPER, opponent);
        }
    },
    PAPER {
        @Override
        public OutCome compete(RoShamBo4 opponent) {
            return compete(ROCK, opponent);
        }
    };

    OutCome compete(RoShamBo4 loser, RoShamBo4 opponent) {
        return (opponent == this) ? OutCome.DRAW
                : ((opponent == loser) ? OutCome.WIN
                : OutCome.LOSE);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            RoShamBo4 a = Enums.random(RoShamBo4.class);
            RoShamBo4 b = Enums.random(RoShamBo4.class);
            System.out.println(a + " vs. "  + b + ": " + a.compete(b));
        }
    }
}
/*
其中，具有两个参数的 compete() 方法执行第二个分发，
该方法执行一系列的比较，其行为类似 switch 语句。

这个版本的程序更简短，不过却比较难理解，
对于一个大型系统而言，
难以理解的代码将导致整个系统不够健壮。
 */
