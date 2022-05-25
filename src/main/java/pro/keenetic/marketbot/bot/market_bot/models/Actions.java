package pro.keenetic.marketbot.bot.market_bot.models;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Actions {
    CONNECT,
    DISCONNECT,
    START,
    STOP
}
