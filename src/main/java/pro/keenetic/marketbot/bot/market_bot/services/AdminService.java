package pro.keenetic.marketbot.bot.market_bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.keenetic.marketbot.bot.market_bot.dao.UserActionDAO;
import pro.keenetic.marketbot.bot.market_bot.models.UserAction;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserActionDAO userActionDAO;

    public List<UserAction> findLast50UserActions() {
        return userActionDAO.findTop50ByOrderByIdDesc();
    }
}
