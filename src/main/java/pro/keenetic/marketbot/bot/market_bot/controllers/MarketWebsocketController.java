package pro.keenetic.marketbot.bot.market_bot.controllers;

import pro.keenetic.marketbot.bot.market_bot.markets.exmo.MarketSecurity;
import pro.keenetic.marketbot.bot.market_bot.websockets.ExmoMarginAuthWebsocket;

public interface MarketWebsocketController {

    void connectWebsocket(MarketSecurity marketSecurity);

    void disconnectWebsocket();

    ExmoMarginAuthWebsocket getExmoMarginAuthWebsocket();
}
