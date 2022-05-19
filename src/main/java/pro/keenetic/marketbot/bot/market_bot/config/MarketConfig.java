package pro.keenetic.marketbot.bot.market_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import pro.keenetic.marketbot.bot.market_bot.controllers.ExmoRestController;
import pro.keenetic.marketbot.bot.market_bot.controllers.ExmoWebsocketController;
import pro.keenetic.marketbot.bot.market_bot.models.Market;
import pro.keenetic.marketbot.bot.market_bot.services.MarketRestService;
import pro.keenetic.marketbot.bot.market_bot.strategies.AbstractStrategy;
import pro.keenetic.marketbot.bot.market_bot.strategies.SimpleStrategyBuy;
import pro.keenetic.marketbot.bot.market_bot.strategies.SimpleStrategySell;
import pro.keenetic.marketbot.bot.market_bot.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MarketConfig {

    @Bean
    @Scope("prototype")
    public SimpleStrategySell simpleStrategySell() {
        return new SimpleStrategySell();
    }

    @Bean
    @Scope("prototype")
    public SimpleStrategyBuy simpleStrategyBuy() {
        return new SimpleStrategyBuy();
    }

    @Bean
    @Scope("prototype")
    public MarketRestService marketRestService() {
        return new MarketRestService(new ExmoWebsocketController(), new ExmoRestController());
    }

    @Bean
    @Scope("prototype")
    public Market market() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Market market = new Market(login, marketRestService());
        List<Strategy> strategies = new ArrayList<>();
        strategies.add(simpleStrategySell());
        strategies.add(simpleStrategyBuy());
        market.setStrategies(strategies);
        return market;
    }
}
