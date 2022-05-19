package pro.keenetic.marketbot.bot.market_bot.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import pro.keenetic.marketbot.bot.market_bot.services.MarketRestService;

public abstract class AbstractStrategy implements Strategy {

    protected MarketRestService marketRestService;

    public void setMarketRestService(MarketRestService marketRestService) {
        this.marketRestService = marketRestService;
    }


}
