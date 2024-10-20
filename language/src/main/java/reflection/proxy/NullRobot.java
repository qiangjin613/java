package reflection.proxy;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

public class NullRobot {

    public static Robot newNullRobot(Class<? extends Robot> type) {
        return (Robot) Proxy.newProxyInstance(NullRobot.class.getClassLoader(), new Class[]{Null.class, Robot.class}, new NullRobotProxyHandler(type));
    }

    public static void main(String[] args) {
        Stream.of(new SnowRobot("SnowRobot"), newNullRobot(SnowRobot.class)).forEach(Robot::test);
    }
}
