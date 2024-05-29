package sk.tuke.gamestudio.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "player")
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Unique
    private String nickname;
    private String password;

    public User(@Unique String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
}

