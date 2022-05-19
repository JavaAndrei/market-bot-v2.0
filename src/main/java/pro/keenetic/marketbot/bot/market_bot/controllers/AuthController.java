package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.services.UserService;
import pro.keenetic.marketbot.bot.market_bot.validator.UserValidatorBeforeCreate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidatorBeforeCreate userValidatorBeforeCreate;

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute("user") User user, BindingResult bindingResult, HttpServletRequest request) {
        user.setRole(userService.getRoleById(1L));
        userValidatorBeforeCreate.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "auth/registration";

        userService.save(user);
        try {
            request.login(user.getUsername(), user.getConfirmPassword());
        } catch (ServletException e) {
            //LOGGER.error("Error while login ", e);
        }
        return "redirect:../users";
    }
}
