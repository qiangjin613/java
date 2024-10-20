package reflection.proxy;

import java.util.List;

/**
 *
 */
public interface Robot {

    String name();

    String model();

    List<Operation> operations();

    static void test(Robot robot) {
        System.out.println();
        if (robot instanceof Null) {
            System.out.println("[Null Robot]");
        }
        System.out.println("Robot name: " + robot.name());
        System.out.println("Robot model: " + robot.model());
        for (Operation operation : robot.operations()) {
            System.out.println(operation.desc.get());
            operation.command.run();
        }
    }
}
