package pro.keenetic.marketbot.bot.market_bot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.keenetic.marketbot.bot.market_bot.dao.UserDAO;
import pro.keenetic.marketbot.bot.market_bot.models.User;
import pro.keenetic.marketbot.bot.market_bot.services.UserService;

@Service("userDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(StringUtils.capitalize(username.toLowerCase()))
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        return SecurityUser.getFromUser(user);
    }


}
