package pro.keenetic.marketbot.bot.market_bot.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne (optional=false)
    @JoinColumn (name="role_id")
    private Role role;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAction> userActions;

    public void setUsername(String username) {
        this.username = StringUtils.capitalize(username.toLowerCase());
    }
}