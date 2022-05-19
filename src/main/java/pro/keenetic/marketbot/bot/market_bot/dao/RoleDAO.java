package pro.keenetic.marketbot.bot.market_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pro.keenetic.marketbot.bot.market_bot.models.Role;

public interface RoleDAO extends JpaRepository<Role, Long> {
}
