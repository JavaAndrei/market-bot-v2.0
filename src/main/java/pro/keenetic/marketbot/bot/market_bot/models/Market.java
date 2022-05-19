package pro.keenetic.marketbot.bot.market_bot.models;


import pro.keenetic.marketbot.bot.market_bot.exceptions.ExmoException;
import pro.keenetic.marketbot.bot.market_bot.services.MarketRestService;
import pro.keenetic.marketbot.bot.market_bot.strategies.Strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Market extends Thread {

    private final MarketRestService marketRestService;
    private List<Strategy> strategies = new ArrayList<>();

    private final String login;
    private final int CYCLE_TIME_MILLISECOND = 1000;

    private boolean isActive;

    public void disable(){
        isActive=false;
    }

    public Market(String login, MarketRestService marketRestService) {
        this.login = login;
        this.marketRestService = marketRestService;
    }

    @Override
    public void run() {
        isActive = true;
        try {
            executeStrategies();
            interruptStrategies();
            marketRestService.disconnectWebsocket();
        } catch (ExmoException e) {
            e.printStackTrace();
        }
    }

    public void executeStrategies() {
        System.out.println("The strategies are started");
        for (Strategy strategy : strategies) {
            strategy.setMarketRestService(marketRestService);
        }
        try {
            long lastTime = new Date().getTime();
            while (isActive) {
                for (Strategy strategy : strategies) {
                    long nowTime = new Date().getTime();
                    while (nowTime - lastTime < CYCLE_TIME_MILLISECOND) {
                        nowTime = new Date().getTime();
                    }
                    lastTime = nowTime;
                    strategy.execute();
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
        }
    }

    public void interruptStrategies() {
        for (int i = 0; i < strategies.size(); i++) {
            strategies.get(i).interrupt();
        }
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    public String getLogin() {
        return login;
    }

    public MarketRestService getMarketRestService() {
        return marketRestService;
    }
}


