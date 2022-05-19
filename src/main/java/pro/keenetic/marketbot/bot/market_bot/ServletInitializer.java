package pro.keenetic.marketbot.bot.market_bot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;


public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(MarketBotApplication.class);
    }
}
