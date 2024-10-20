package reflection.proxy;

import java.util.function.Supplier;

/**
 * 包含一个描述和命令，是一个命令模式
 */
public class Operation {

    public final Supplier<String> desc;

    public final Runnable command;

    public Operation(Supplier<String> desc, Runnable command) {
        this.desc = desc;
        this.command = command;
    }
}
