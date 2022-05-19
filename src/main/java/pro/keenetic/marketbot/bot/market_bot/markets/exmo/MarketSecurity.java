package pro.keenetic.marketbot.bot.market_bot.markets.exmo;

public interface MarketSecurity {

    String signWS(String postData);

    String signRest(String postData);

    String getPublicKey();

}
