package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pro.keenetic.marketbot.bot.market_bot.services.AdminService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class RootController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String getMainPage(Principal principal, HttpServletRequest request) {

        return "redirect:/trader";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", adminService.findLast50UserActions());
        return "views/admin";
    }

    @GetMapping("/guest")
    public String guest() {
        return "views/guest";
    }

    @PostMapping("/guest")
    public String chosePage(HttpServletRequest request) {
        if (request.getParameter("action").equals("admin_page")) {
            return "redirect:/admin";
        } if (request.getParameter("action").equals("trader_page")) {
            return "redirect:/trader";
        }
        return "views/guest";
    }

    @GetMapping("/trader")
    public String trader() {
        return "views/trader";
    }
}
