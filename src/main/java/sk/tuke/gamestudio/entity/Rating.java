package sk.tuke.gamestudio.entity;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue
    private int id;
    private String player;
    private String game;
    private int rating;
    private Date ratedOn;

    public Rating(String player, String game, int rating, Date ratedOn) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

}