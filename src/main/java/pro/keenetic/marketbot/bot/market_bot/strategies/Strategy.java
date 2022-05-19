package pro.keenetic.marketbot.bot.market_bot.strategies;

import pro.keenetic.marketbot.bot.market_bot.models.Market;
import pro.keenetic.marketbot.bot.market_bot.services.MarketRestService;

public interface Strategy {

    void execute();

    void interrupt();

    void setMarketRestService(MarketRestService marketRestService);

}
