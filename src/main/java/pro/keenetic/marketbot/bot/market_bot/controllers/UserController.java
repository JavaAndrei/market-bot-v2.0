package pro.keenetic.marketbot.bot.market_bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pro.keenetic.marketbot.bot.market_bot.models.Role;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.services.UserService;
import pro.keenetic.marketbot.bot.market_bot.validator.UserValidatorBeforeCreate;
import pro.keenetic.marketbot.bot.market_bot.validator.UserValidatorBeforeUpdate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidatorBeforeCreate userValidatorBeforeCreate;

    @Autowired
    private UserValidatorBeforeUpdate userValidatorBeforeUpdate;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "views/users/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "views/users/show";
    }

    @GetMapping("/new")
    //@PreAuthorize("hasAnyAuthority('permissions:all', 'permissions:observer')")
    public String newUser(@ModelAttribute("user") User user) {

        return "views/users/new";
    }

    @PostMapping()
    //@PreAuthorize("hasAuthority('permissions:all')")
    public String create(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidatorBeforeCreate.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "views/users/new";

        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    //@PreAuthorize("hasAnyAuthority('permissions:all', 'permissions:observer')")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getById(id));
        return "views/users/edit";
    }

    @PatchMapping("/{id}")
    //@PreAuthorize("hasAuthority('permissions:all')")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id, BindingResult bindingResult) {
        userValidatorBeforeUpdate.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "views/users/edit";

        userService.update(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('permissions:admin')")
    public String delete(@PathVariable("id") long id, HttpServletRequest request) {
        System.out.println(request.getHeader(HttpHeaders.ORIGIN));
        if (request.getParameter("action").equals("mark_deleted")) {
            User user = userService.getById(id);
            user.setDeleted(true);
            userService.update(user);
        } else if (request.getParameter("action").equals("delete_user")) {
            userService.deleteById(id);
        }
        return "redirect:/users";
    }

    @ModelAttribute("roles")
    private List<Role> getRoles() {

        return userService.findAllRoles();
    }

}
