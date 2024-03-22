package sk.tuke.gamestudio.game.checkers.core;

import java.io.Serializable;

public class Move implements Serializable{
    private final Piece from;
    private final Tile to;
    private boolean captured;

    public Move(Piece from, Tile to) {
        this.from = from;
        this.to = to;
    }

    public Piece getFromTile() {
        return from;
    }

    public Tile getToTile() {
        return to;
    }

    public boolean isCaptured() {
        return captured;
    }

    public void setCaptured(boolean captured) {
        this.captured = captured;
    }

}
