package pro.keenetic.marketbot.bot.market_bot.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
@Documented
public @interface Login {

    String message() default "{Such username already exists}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
