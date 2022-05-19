package pro.keenetic.marketbot.bot.market_bot.markets;

import pro.keenetic.marketbot.bot.market_bot.exceptions.ConnectionPoolException;
import pro.keenetic.marketbot.bot.market_bot.models.Market;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionPool {

    private static int maxSize;
    public static final Map<String, Market> storage;

    static  {
        maxSize = 4;
        storage = new ConcurrentHashMap<>(maxSize);
    }

    public static Market getMarket(String login) {
        if (storage.containsKey(login)) {
            return storage.get(login);
        }
        return null;
    }

    public static void addMarket(String login, Market market) throws ConnectionPoolException {
        if (storage.size() >= maxSize) {
            throw new ConnectionPoolException("Can't connect. No free connections");
        }
        storage.put(login, market);
    }

    public static boolean removeMarket(String login) {
        if (storage.containsKey(login)) {
            storage.remove(login);
            return true;
        }
        return false;
    }

    public static boolean isMarketRunning(String login) {
        return storage.containsKey(login);
    }
}
