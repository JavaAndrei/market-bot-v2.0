package pro.keenetic.marketbot.bot.market_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class MarketBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketBotApplication.class, args);
    }
}
