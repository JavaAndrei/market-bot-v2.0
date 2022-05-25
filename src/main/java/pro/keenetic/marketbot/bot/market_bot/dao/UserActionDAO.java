package pro.keenetic.marketbot.bot.market_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.models.UserAction;

import java.util.List;
import java.util.Optional;

public interface UserActionDAO extends JpaRepository<UserAction, Long> {

    List<UserAction> findTop50ByOrderByIdDesc();

    //void deleteAllByUser(User user);
}
