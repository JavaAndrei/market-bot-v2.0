package pro.keenetic.marketbot.bot.market_bot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.keenetic.marketbot.bot.market_bot.models.Action;

public interface ActionDAO extends JpaRepository<Action, Long> {
}
