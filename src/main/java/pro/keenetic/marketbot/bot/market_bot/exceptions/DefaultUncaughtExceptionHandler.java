package pro.keenetic.marketbot.bot.market_bot.exceptions;

public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("DefaultUncaughtException: " + t.getName() + ", " + e.getMessage());
    }
}
