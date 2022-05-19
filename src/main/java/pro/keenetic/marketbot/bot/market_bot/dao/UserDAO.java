package pro.keenetic.marketbot.bot.market_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pro.keenetic.marketbot.bot.market_bot.models.User;

import java.util.Optional;

public interface UserDAO extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
