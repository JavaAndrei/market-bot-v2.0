package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.keenetic.marketbot.bot.market_bot.json.BinancePrice;
import pro.keenetic.marketbot.bot.market_bot.json.ExmoOrderBook;
import pro.keenetic.marketbot.bot.market_bot.markets.ConnectionPool;
import pro.keenetic.marketbot.bot.market_bot.services.BotService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequestMapping("/bots")
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private ExmoOrderBook exmoOrderBook;

    @Autowired
    private BinancePrice binancePrice;

    @GetMapping("/connection")
    public String getMarketKeysPage() {
        return "views/bots/connection";
    }

    @PostMapping("/connection")
    public String getBotPage(HttpServletRequest request) {
        botService.startBot(request.getParameter("public_key"), request.getParameter("secret_key"));
        return "views/bots/bot";
    }

    @GetMapping("/bot")
    public String selectBotPage(Principal principal) {
        if (ConnectionPool.isMarketRunning(principal.getName())) {
            return "views/bots/bot";
        } else {
            return "views/bots/connection";
        }
    }

    @PostMapping("/bot")
    public String getConnectionPage() {
        botService.stopBot();
        return "views/bots/connection";
    }

    @GetMapping("/service")
    public String service(Model model) {
        model.addAttribute("price", binancePrice);
        model.addAttribute("orderBook", exmoOrderBook);
        return "views/bots/service";
    }

    @GetMapping("/trading")
    public String getTrading(Model model, Principal principal) {
        model.addAttribute("price", binancePrice);
        model.addAttribute("market", ConnectionPool.getMarket(principal.getName()).getMarketRestService());
        return "views/bots/trading";
    }
}
