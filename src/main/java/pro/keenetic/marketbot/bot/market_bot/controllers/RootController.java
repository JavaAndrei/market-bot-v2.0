package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/guest")
    public String guest() {
        return "guest";
    }

    @GetMapping("/trader")
    public String trader() {
        return "trader";
    }

    public static interface MarketAuthWebsocket {
    }
}
