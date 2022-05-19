package pro.keenetic.marketbot.bot.market_bot.services;

import pro.keenetic.marketbot.bot.market_bot.controllers.MarketRestController;
import pro.keenetic.marketbot.bot.market_bot.controllers.MarketWebsocketController;
import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;
import pro.keenetic.marketbot.bot.market_bot.markets.exmo.MarketSecurity;
import pro.keenetic.marketbot.bot.market_bot.websockets.ExmoMarginAuthWebsocket;

import java.util.Map;

public class MarketRestService {

    MarketWebsocketController websocketController;
    MarketRestController restController;

    private Map<String, Object> walletUSDT;
    private Map<String, Object> position;
    private Map<String, Object> limitOrderSell;
    private Map<String, Object> limitOrderBuy;

    public MarketRestService(MarketWebsocketController websocketController, MarketRestController restController) {
        this.websocketController = websocketController;
        this.restController = restController;

        ExmoMarginAuthWebsocket exmoMarginAuthWebsocket = websocketController.getExmoMarginAuthWebsocket();
        walletUSDT = exmoMarginAuthWebsocket.getWalletUSDT();
        position = exmoMarginAuthWebsocket.getPosition();
        limitOrderSell = exmoMarginAuthWebsocket.getLimitOrderSell();
        limitOrderBuy = exmoMarginAuthWebsocket.getLimitOrderBuy();
    }

    public double getQuantityShortPosition() {
        if (position.containsKey("closed")
                && !((Boolean) position.get("closed"))
                && position.containsKey("type")
                && position.get("type").equals("short")) {
            return Double.parseDouble(position.containsKey("quantity") ? (String) position.get("quantity") : "0.0");
        }
        return 0.0;
    }

    public double getQuantityLongPosition() {
        if (position.containsKey("closed")
                && !((Boolean) position.get("closed"))
                && position.containsKey("type")
                && position.get("type").equals("long")) {
            return Double.parseDouble(position.containsKey("quantity") ? (String) position.get("quantity") : "0.0");
        }
        return 0.0;
    }

    public double getQuantityBalance(double price, int leverage) {
        double balance = Double.parseDouble(walletUSDT.containsKey("balance") ? (String) walletUSDT.get("balance") : "0.0");
        return balance * leverage / price;
    }

    public String createOrderSell(double price, double quantity, int leverage) throws ExmoException {
        String status = limitOrderSell.containsKey("status") ? (String) limitOrderSell.get("status") : "";
        String order_id = limitOrderSell.containsKey("order_id") ? (String) limitOrderSell.get("order_id") : "";
        if (quantity >= 0.00002) {
            //System.out.println("status: " + status);
            if (status != null && status.equals("active")) {
                return restController.orderUpdate(order_id, quantity, price);
            } else {
                return restController.orderSellCreate(leverage, quantity, price);
            }
        } else {
            if (status != null && status.equals("active")) {
                return restController.orderCancel(order_id);
            }
        }
        return "Order hasn't created.";
    }

    public String createOrderBuy(double price, double quantity, int leverage) throws ExmoException {
        String status = limitOrderBuy.containsKey("status") ? (String) limitOrderBuy.get("status") : "";
        String order_id = limitOrderBuy.containsKey("order_id") ? (String) limitOrderBuy.get("order_id") : "";
        if (quantity >= 0.00002) {
            //System.out.println("status: " + status);
            if (status != null && status.equals("active")) {
                return restController.orderUpdate(order_id, quantity, price);
            } else {
                return restController.orderBuyCreate(leverage, quantity, price);
            }
        } else {
            if (status != null && status.equals("active")) {
                return restController.orderCancel(order_id);
            }
        }
        return "Order hasn't created.";
    }

    public String orderSellCancel() throws ExmoException {
        if (limitOrderSell.containsKey("order_id")) {
            String order_id = (String) limitOrderSell.get("order_id");
            if (order_id != null && !order_id.equals(""))
                return restController.orderCancel(order_id);
            else
                return "limitOrderSell hasn't been found";
        }
        return "limitOrderSell doesn't contain \"order_id\"";
    }

    public String orderBuyCancel() throws ExmoException {
        if (limitOrderBuy.containsKey("order_id")) {
            String order_id = (String) limitOrderBuy.get("order_id");
            if (order_id != null && !order_id.equals(""))
                return restController.orderCancel(order_id);
            else
                return "limitOrderBuy hasn't been found";
        }
        return "limitOrderBuy doesn't contain \"order_id\"";
    }

    public void initSecurity(MarketSecurity marketSecurity) {
        websocketController.connectWebsocket(marketSecurity);
        restController.connectRestAPI(marketSecurity);
    }

    public void disconnectWebsocket() {
        websocketController.disconnectWebsocket();
    }

    public Map<String, Object> getWalletUSDT() {
        return walletUSDT;
    }

    public Map<String, Object> getPosition() {
        return position;
    }

    public Map<String, Object> getLimitOrderSell() {
        return limitOrderSell;
    }

    public Map<String, Object> getLimitOrderBuy() {
        return limitOrderBuy;
    }
}