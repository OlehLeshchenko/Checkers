package sk.tuke.gamestudio.entity;

import lombok.*;
import sk.tuke.gamestudio.game.checkers.core.Field;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Save {
    @Id
    @GeneratedValue
    private int id;
    private String player;
    private String game;
    private Field save;
    private Date savedOn;

    public Save(String player, String game, Field save, Date savedOn) {
        this.player = player;
        this.game = game;
        this.save = save;
        this.savedOn = savedOn;
    }

}
