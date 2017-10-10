package chp03;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LazyExecute {
    public static void main(String[] args) {
        info(Logger.getLogger(""), () -> "hello");
    }

    //实现延迟记录日志信息
    private static void info(Logger logger, Supplier<String> message) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(message.get());
        }
    }
}
