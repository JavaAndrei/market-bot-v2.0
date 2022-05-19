package pro.keenetic.marketbot.bot.market_bot.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.services.UserService;

@Component
public class UserValidatorBeforeUpdate implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        User updatedUser = (User) target;

        User user = userService.findByUsername(updatedUser.getUsername());

        if (user == null) {
            errors.rejectValue("username", "888", "It's impossible to change Login here");
        } else {
            if (!updatedUser.getPassword().equals(user.getPassword())) {
                errors.rejectValue("password", "888", "It's impossible to change Password here");
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", "777", "Role is required");
    }
}
