package pro.keenetic.marketbot.bot.market_bot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import pro.keenetic.marketbot.bot.market_bot.json.BinancePrice;
import pro.keenetic.marketbot.bot.market_bot.json.ExmoOrderBook;
import pro.keenetic.marketbot.bot.market_bot.websockets.BinanceWebsocket;
import pro.keenetic.marketbot.bot.market_bot.websockets.ExmoMarginAuthWebsocket;
import pro.keenetic.marketbot.bot.market_bot.websockets.ExmoWebsocket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:websocket.properties")
public class WebsocketConfig {

    @Bean
    public BinancePrice binancePrice() {
        return new BinancePrice();
    }

    @Bean
    public BinanceWebsocket binanceWebsocket(@Value("${websocket.binance.endpoint}") String endpoint) {
        return new BinanceWebsocket(endpoint, binancePrice());
    }

    @Bean
    public ExmoOrderBook exmoOrderBook() {
        return new ExmoOrderBook();
    }

    @Bean
    public ExmoWebsocket exmoWebsocket(@Value("${websocket.exmo.endpoint}") String endpoint,
                                       @Value("#{${websocket.exmo.messages}}") List<String> messages) {
        ExmoWebsocket exmoWebsocket = new ExmoWebsocket(endpoint, exmoOrderBook());
        exmoWebsocket.setMessages(messages);
        return exmoWebsocket;
    }

    @Bean//(name = "exmoMarginAuthWebsocket")
    @Scope("prototype")
    public ExmoMarginAuthWebsocket exmoMarginAuthWebsocket(@Value("${websocket.exmo_margin_auth.endpoint}") String endpoint,
                                                           @Value("${websocket.exmo_margin_auth.messages1}") String message1,
                                                           @Value("${websocket.exmo_margin_auth.messages2}") String message2) {
        List<String> messages= new ArrayList<>();
        messages.add(message1);
        messages.add(message2);
        ExmoMarginAuthWebsocket exmoMarginAuthWebsocket = new ExmoMarginAuthWebsocket(endpoint);
        exmoMarginAuthWebsocket.setMessages(messages);
        return exmoMarginAuthWebsocket;
    }
}
