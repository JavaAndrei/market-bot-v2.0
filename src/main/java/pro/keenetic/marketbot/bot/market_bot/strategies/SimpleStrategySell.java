package pro.keenetic.marketbot.bot.market_bot.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;
import pro.keenetic.marketbot.bot.market_bot.json.BinancePrice;

public class SimpleStrategySell extends AbstractStrategy {

    @Autowired
    private BinancePrice binancePrice;

    private double targetPrice;
    private double delta;
    private double price;

    private int leverage = 2;

    private double getBinancePrice() {
        return binancePrice.getValue();
    }

    private double getPrice(double price, double delta) {
        return price + delta;
    }

    private double getDelta() {
        return 500.0;
    }

    private void prepareData() {
        delta = getDelta();
        targetPrice = getBinancePrice();
        price = getPrice(targetPrice, delta);
    }

    @Override
    public void execute() {
        //System.out.println("The simple strategy sell is started");
        prepareData();
        double quantityBalance = marketRestService.getQuantityBalance(price, leverage);
        double quantityShort = marketRestService.getQuantityShortPosition();
        try {
            marketRestService.createOrderSell(price, quantityBalance - quantityShort, leverage);
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        System.out.println("The simple strategy sell is stopped");
        try {
            System.out.println(marketRestService.orderSellCancel());
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }
}
