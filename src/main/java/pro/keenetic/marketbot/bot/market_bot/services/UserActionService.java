package pro.keenetic.marketbot.bot.market_bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.keenetic.marketbot.bot.market_bot.dao.ActionDAO;
import pro.keenetic.marketbot.bot.market_bot.dao.UserActionDAO;
import pro.keenetic.marketbot.bot.market_bot.dao.UserDAO;
import pro.keenetic.marketbot.bot.market_bot.models.Action;
import pro.keenetic.marketbot.bot.market_bot.models.Actions;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.models.UserAction;

import java.util.Date;
import java.util.List;

@Service
public class UserActionService {

    @Autowired
    private UserActionDAO userActionDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ActionDAO actionDAO;

    public void save(String username, Actions actions) {
        UserAction userAction = new UserAction();
        userAction.setUser(userDAO.findByUsername(StringUtils.capitalize(username.toLowerCase())).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist")));
        userAction.setAction(actionDAO.getById((long) actions.ordinal() + 1));
        userAction.setDate(new Date());
        userActionDAO.save(userAction);
    }
}
