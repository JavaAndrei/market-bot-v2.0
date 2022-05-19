package pro.keenetic.marketbot.bot.market_bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pro.keenetic.marketbot.bot.market_bot.dao.RoleDAO;
import pro.keenetic.marketbot.bot.market_bot.dao.UserDAO;
import pro.keenetic.marketbot.bot.market_bot.models.Role;
import pro.keenetic.marketbot.bot.market_bot.models.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user) {
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    public void update(User user) {
        user.setUsername(user.getUsername());
        if (!user.getPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userDAO.save(user);
    }

    public List<User> findAll() {

        return userDAO.findAll();
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(StringUtils.capitalize(username.toLowerCase())).orElse(null);
    }

    public User getById(long id) {

        return userDAO.getById(id);
    }

    public void deleteById(long id) {

        userDAO.deleteById(id);
    }

    public Role getRoleById(long id) {
        return roleDAO.getById(id);
    }

    public List<Role> findAllRoles() {
        return roleDAO.findAll();
    }
}
