package pro.keenetic.marketbot.bot.market_bot.json;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;

@Getter
@Setter
public class ExmoOrderBook {

    private long time;

    private List<List<Double>> ask;
    private List<List<Double>> bid;

    @Override
    public String toString() {
        return "OrderBook   {" +
                "time=" + time +
                ", ask=" + ask.get(0) +
                ", bid=" + bid.get(0) +
                '}';
    }
}
