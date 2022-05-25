package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidatorBeforeCreate userValidatorBeforeCreate;

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        String error = (String) httpSession.getAttribute("error");
        httpSession.removeAttribute("error");
        if (error != null) {
            String username = (String) httpSession.getAttribute("username");
            httpSession.removeAttribute("username");
            if (userService.findByUsername(username) == null) {
                model.addAttribute("username_error", error + ". Login doesn't exist.");
            } else {
                model.addAttribute("password_error", error + ". Incorrect password.");
            }
            model.addAttribute("username", username);
        }
        return "views/auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user) {
        return "views/auth/registration";
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute("user") User user, BindingResult bindingResult, HttpServletRequest request) {
        user.setRole(userService.getRoleById(1L));
        user.setDeleted(false);
        userValidatorBeforeCreate.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "views/auth/registration";

        userService.save(user);
        try {
            request.login(user.getUsername(), user.getConfirmPassword());
        } catch (ServletException e) {
            //LOGGER.error("Error while login ", e);
        }
        return "redirect:../trader";
    }
}
