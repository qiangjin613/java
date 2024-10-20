package reflection.proxy;

import com.sun.jmx.snmp.internal.SnmpAccessControlModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 扫雪机器人
 */
public class SnowRobot implements Robot{

    private String name;

    private List<Operation> ops= Arrays.asList(
        new Operation(() -> name + "可以扫雪", () -> System.out.println(name + " 正在扫雪")),
        new Operation(() -> name + "可以扫冰雪", () -> System.out.println(name + " 正在扫冰雪"))
    );

    public SnowRobot(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String model() {
        return "马克 11";
    }

    @Override
    public List<Operation> operations() {
        return ops;
    }

    public static void main(String[] args) {
        Robot.test(new SnowRobot("阿波罗"));
    }
}
