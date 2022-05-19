package pro.keenetic.marketbot.bot.market_bot.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.services.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class LoginValidator implements ConstraintValidator<Login, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (userService == null)
            return false;
        User user = userService.findByUsername(s);
        if (user == null) {
            return true;
        }
        return false;
    }
}
