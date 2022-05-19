package pro.keenetic.marketbot.bot.market_bot.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pro.keenetic.marketbot.bot.market_bot.ApplicationContextHolder;
import pro.keenetic.marketbot.bot.market_bot.markets.ConnectionPool;
import pro.keenetic.marketbot.bot.market_bot.models.Market;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.ExmoSecurity;

@Service
public class BotService {

    public void startBot(String publicKey, String secretKey) {

        Market market = ApplicationContextHolder.getBean("market");
        market.getMarketRestService().initSecurity(new ExmoSecurity(publicKey, secretKey));

        ConnectionPool.addMarket(market.getLogin(), market);

        market.start();
    }

    public void stopBot() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ConnectionPool.isMarketRunning(login)) {
            Market market = ConnectionPool.getMarket(login);
            market.disable();

            while (market.isAlive()) {
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ConnectionPool.removeMarket(login);
        }
    }
}
