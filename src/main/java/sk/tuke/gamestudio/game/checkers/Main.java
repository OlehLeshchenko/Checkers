package sk.tuke.gamestudio.game.checkers;

import sk.tuke.gamestudio.game.checkers.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.checkers.core.Field;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        ConsoleUI userInterface = new ConsoleUI(field);
        userInterface.play();
    }
}