import java.util.EnumMap;

/*
使用 EnumMap 能够实现“真正的”两路分发。
EnumMap 是为 enum 专门设计的一种性能非常好的特殊 Map。
由于我们的目的是摸索出两种未知的类型，
所以可以用一个 EnumMap 的 EnumMap 来实现两路分发：
 */
enum RoShamBo5 implements Competitor<RoShamBo5> {
    PAPER, SCISSORS, ROCK;
    static EnumMap<RoShamBo5, EnumMap<RoShamBo5, OutCome>>
            table = new EnumMap<>(RoShamBo5.class);
    static {
        for (RoShamBo5 it : RoShamBo5.values()) {
            table.put(it, new EnumMap<>(RoShamBo5.class));
        }
        initRow(PAPER, OutCome.DRAW, OutCome.LOSE, OutCome.WIN);
        initRow(SCISSORS, OutCome.WIN, OutCome.DRAW, OutCome.LOSE);
        initRow(ROCK, OutCome.LOSE, OutCome.WIN, OutCome.DRAW);
    }
    static void initRow(RoShamBo5 it, OutCome vPAPER, OutCome vSCISSORS, OutCome vROCK) {
        EnumMap<RoShamBo5, OutCome> row = RoShamBo5.table.get(it);
        row.put(RoShamBo5.PAPER, vPAPER);
        row.put(RoShamBo5.SCISSORS, vSCISSORS);
        row.put(RoShamBo5.ROCK, vROCK);
    }

    @Override
    public OutCome compete(RoShamBo5 it) {
        return table.get(this).get(it);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            RoShamBo3 a = Enums.random(RoShamBo3.class);
            RoShamBo3 b = Enums.random(RoShamBo3.class);
            System.out.println(a + " vs. "  + b + ": " + a.compete(b));
        }
    }
}
/*
该程序在一个 static 子句中初始化 EnumMap 对象，
具体见表格似的 initRow() 方法调用。

请注意 compete() 方法，您可以看到，在一行语句中发生了两次分发。
 */
