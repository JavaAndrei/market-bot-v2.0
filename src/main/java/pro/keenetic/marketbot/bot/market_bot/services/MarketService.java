package pro.keenetic.marketbot.bot.market_bot.services;

import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;

public interface MarketService {

    String orderBuyCreate(int leverage, double quantity, double price) throws ExmoException;

    String orderSellCreate(int leverage, double quantity, double price) throws ExmoException;

    String orderUpdate(String order_id, double quantity, double price) throws ExmoException;

    String orderCancel(String order_id) throws ExmoException;
}
