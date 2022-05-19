package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pro.keenetic.marketbot.bot.market_bot.ApplicationContextHolder;
import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.MarketSecurity;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.RestAPI;

import java.util.HashMap;

@RestController
public class ExmoRestController implements MarketRestController {

    private RestAPI restAPI;

    private final String pair = "BTC_USDT";

    public ExmoRestController() {
        this.restAPI = ApplicationContextHolder.getBean("restAPI");
    }

    public void connectRestAPI(MarketSecurity marketSecurity){
        restAPI.setMarketSecurity(marketSecurity);
    }

    public String orderBuyCreate(int leverage, double quantity, double price) throws ExmoException {
        String result = restAPI.request("margin/user/order/create", new HashMap<String, String>() {{
            put("pair", pair);
            put("leverage", String.valueOf(leverage));
            put("type", "limit_buy");
            put("quantity", String.format("%.6f", Math.floor(quantity * 1000000) / 1000000));
            put("price", String.format("%.2f", price));
        }});
        return result;
    }

    public String orderSellCreate(int leverage, double quantity, double price) throws ExmoException {
        String result = restAPI.request("margin/user/order/create", new HashMap<String, String>() {{
            put("pair", pair);
            put("leverage", String.valueOf(leverage));
            put("type", "limit_sell");
            put("quantity", String.format("%.8f", Math.floor(quantity * 100000000) / 100000000));
            put("price", String.format("%.2f", price));
        }});
        return result;
    }

    public String orderUpdate(String order_id, double quantity, double price) throws ExmoException {
        String result = restAPI.request("margin/user/order/update", new HashMap<String, String>() {{
            put("order_id", order_id);
            put("quantity", String.format("%.8f", Math.floor(quantity * 100000000) / 100000000));
            put("price", String.format("%.2f", price));
        }});
        return result;
    }

    public String orderCancel(String order_id) throws ExmoException {
        String result = restAPI.request("margin/user/order/cancel", new HashMap<String, String>() {{
            put("order_id", order_id);
        }});
        return result;
    }

    public String userInfo() throws ExmoException {
        String result = restAPI.request("margin/user/info", null);
        return result;
    }

    public String walletList() throws ExmoException {
        String result = restAPI.request("margin/user/wallet/list", null);
        return result;
    }

    public String positionList() throws ExmoException {
        String result = restAPI.request("margin/user/position/list", null);
        return result;
    }
}
