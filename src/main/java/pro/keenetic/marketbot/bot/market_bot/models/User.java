package pro.keenetic.marketbot.bot.market_bot.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    public void setUsername(String username) {
        this.username = StringUtils.capitalize(username.toLowerCase());
    }
}