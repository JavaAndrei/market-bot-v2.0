package pro.keenetic.marketbot.bot.market_bot.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user_actions")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn (name="user_id")
    private User user;

    @ManyToOne(optional=false)
    @JoinColumn (name="action_id")
    private Action action;

    @Column(name = "date_time", nullable = false)
    private Date date;
}
