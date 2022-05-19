package pro.keenetic.marketbot.bot.market_bot.controllers;

import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.MarketSecurity;

import java.util.HashMap;

public interface MarketRestController {

    String orderBuyCreate(int leverage, double quantity, double price) throws ExmoException;

    String orderSellCreate(int leverage, double quantity, double price) throws ExmoException;

    String orderUpdate(String order_id, double quantity, double price) throws ExmoException;

    String orderCancel(String order_id) throws ExmoException;

    String userInfo() throws ExmoException;

    String walletList() throws ExmoException;

    String positionList() throws ExmoException;

    void connectRestAPI(MarketSecurity marketSecurity);
}
