package sk.tuke.gamestudio.entity;

import sk.tuke.gamestudio.game.checkers.core.Field;

import java.util.Date;

public class Save {
    private String player;

    private String game;

    private Field save;

    private Date savedAt;

    private int playedTime;

    public Save(String player, String game, Field save, Date savedAt, int playedTime) {
        this.player = player;
        this.game = game;
        this.save = save;
        this.savedAt = savedAt;
        this.playedTime = playedTime;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public Field getSave() {
        return save;
    }

    public void setSave(Field save) {
        this.save = save;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public int getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(int playedTime) {
        this.playedTime = playedTime;
    }
}
