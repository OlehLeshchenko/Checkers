package sk.tuke.gamestudio.game.checkers.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
public class Move implements Serializable {
    private final Piece from;
    private final Tile to;
    @Setter
    private boolean captured;

    public Move(Piece from, Tile to) {
        this.from = from;
        this.to = to;
    }

}
