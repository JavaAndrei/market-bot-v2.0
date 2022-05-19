package pro.keenetic.marketbot.bot.market_bot.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;
import pro.keenetic.marketbot.bot.market_bot.json.BinancePrice;

public class SimpleStrategyBuy extends AbstractStrategy {

    @Autowired
    private BinancePrice binancePrice;

    private double targetPrice;
    private double delta;
    private double price;
    private int leverage = 2;

    private double getTargetPrice() {
        return binancePrice.getValue();
    }

    private double getPrice(double price, double delta) {
        return price - delta;
    }

    private double getDelta() {
        return 500.0;
    }

    private void prepareData() {
        delta = getDelta();
        targetPrice = getTargetPrice();
        price = getPrice(targetPrice, delta);
    }

    @Override
    public void execute() {
        //System.out.println("The simple strategy buy is started");
        prepareData();
        double quantityBalance = marketRestService.getQuantityBalance(price, leverage);
        double quantityLong = marketRestService.getQuantityLongPosition();
        try {
            marketRestService.createOrderBuy(price, quantityBalance - quantityLong, leverage);
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
        System.out.println("The simple strategy buy is stopped");
        try {
            System.out.println(marketRestService.orderBuyCancel());
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }
}
