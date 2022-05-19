package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.stereotype.Controller;
import pro.keenetic.marketbot.bot.market_bot.ApplicationContextHolder;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.MarketSecurity;
import pro.keenetic.marketbot.bot.market_bot.websockets.ExmoMarginAuthWebsocket;

@Controller
public class ExmoWebsocketController implements MarketWebsocketController {

    private final ExmoMarginAuthWebsocket exmoMarginAuthWebsocket;

    public ExmoWebsocketController() {
        this.exmoMarginAuthWebsocket = ApplicationContextHolder.getBean("exmoMarginAuthWebsocket");
    }

    public void connectWebsocket(MarketSecurity marketSecurity) {
        exmoMarginAuthWebsocket.connect(marketSecurity);
    }

    public void disconnectWebsocket() {
        exmoMarginAuthWebsocket.disconnect();
    }

    public ExmoMarginAuthWebsocket getExmoMarginAuthWebsocket() {
        return exmoMarginAuthWebsocket;
    }
}
