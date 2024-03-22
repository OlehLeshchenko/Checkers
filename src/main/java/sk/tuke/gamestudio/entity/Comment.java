package sk.tuke.gamestudio.entity;

import java.util.Date;

public class Comment {
    private String player;

    private String game;

    private String text;

    private Date commentedAt;

    public Comment(String player, String game, String text, Date commentedAt) {
        this.player = player;
        this.game = game;
        this.text = text;
        this.commentedAt = commentedAt;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date commentedAt) {
        this.commentedAt = commentedAt;
    }

    @Override
    public String toString() {
        return "Comment{" + "player='" + player + '\'' + ", game='" + game + '\'' + ", text='" + text + '\'' + ", commentedAt=" + commentedAt + '}';
    }


}
