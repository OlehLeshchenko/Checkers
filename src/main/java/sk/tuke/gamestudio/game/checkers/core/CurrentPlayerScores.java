package sk.tuke.gamestudio.game.checkers.core;

import lombok.Getter;

@Getter
public class CurrentPlayerScores {
    private final int commonersCount;
    private final int kingsCount;
    private final int minutes;

    public CurrentPlayerScores(int commonersCount, int kings, int minutes) {
        this.commonersCount = commonersCount;
        this.kingsCount = kings;
        this.minutes = minutes;
    }

    public int getCommonerPoints() {
        return commonersCount * 75;
    }

    public int getKingPoints() {
        return calculateKingPoints(kingsCount);
    }

    public int getTimePoints() {
        return Math.max(10000 - minutes * 665, 0);
    }
    private int calculateKingPoints(int numberOfKings) {
        int points = 0;
        int kingValue = 150;

        for (int i = 0; i < numberOfKings; i++) {
            points += kingValue;
            kingValue *= 2;
        }

        return points;
    }

}
