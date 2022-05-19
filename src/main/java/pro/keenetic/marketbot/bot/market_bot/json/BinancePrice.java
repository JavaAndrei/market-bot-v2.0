package pro.keenetic.marketbot.bot.market_bot.json;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class BinancePrice {

    private long time;
    private double value;

    @Override
    public String toString() {
        return "BinancePrice{" +
                "time=" + time +
                ", value=" + value +
                '}';
    }
}
