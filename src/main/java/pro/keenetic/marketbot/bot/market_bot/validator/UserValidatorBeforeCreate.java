package pro.keenetic.marketbot.bot.market_bot.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.*;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.services.UserService;

@Component
public class UserValidatorBeforeCreate implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "777", "Login is required");
        if (!errors.hasFieldErrors("username")) {
            if (user.getUsername().length() < 2 || user.getUsername().length() > 32) {
                errors.rejectValue("username", "777", "Login should be between 2 and 32 characters");
            } else if (!user.getUsername().matches("[A-Za-z].+")) {
                errors.rejectValue("username", "777", "Login should start with a letter");
            } else if (!user.getUsername().matches("\\w+")) {
                errors.rejectValue("username", "777", "Login may contain symbols \"'A-Z'; 'a-z'; '0-9'; '_'\"");
            } else if (userService != null && userService.findByUsername(user.getUsername()) != null) {
                errors.rejectValue("username", "777", "Such Login already exists");
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "777", "Password is required");
        if (!errors.hasFieldErrors("password")) {
            if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
                errors.rejectValue("password", "777", "Password should be between 8 and 32 characters");
            } else if (!user.getPassword().matches("\\w+")) {
                errors.rejectValue("password", "777", "Password may contain symbols \"'A-Z'; 'a-z'; '0-9'; '_'\"");
            }
        }

        if (user.getConfirmPassword() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "777", "Password confirmation required");
            if (!errors.hasFieldErrors("confirmPassword")) {
                if (!user.getPassword().equals(user.getConfirmPassword())) {
                    errors.rejectValue("confirmPassword", "777", "Passwords aren't equal");
                }
            }

        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "777", "Role is required");
    }
}
