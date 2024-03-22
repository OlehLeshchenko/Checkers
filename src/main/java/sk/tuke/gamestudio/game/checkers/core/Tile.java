package sk.tuke.gamestudio.game.checkers.core;


import java.io.Serializable;

public class Tile implements Serializable {
    private final int posX;
    private final int posY;

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


}
