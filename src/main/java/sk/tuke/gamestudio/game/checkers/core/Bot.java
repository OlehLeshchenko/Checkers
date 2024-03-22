package sk.tuke.gamestudio.game.checkers.core;


import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Bot implements Serializable {

    private boolean active = false;
    private static final Random random = new Random();

    public Bot() {
    }
    public void makeMove(Field field){
        if(field.getState().equals(GameState.PLAYING) && field.getCurrentPlayer().equals(PieceColor.BLACK))
            field.makeMove(chooseMove(field));
    }


    private Move chooseMove(Field field) {
        List<Piece> capturingPieces = field.getCapturingPieces(field.getCurrentPlayer());

        if (!capturingPieces.isEmpty()) {
            Piece randomPiece = getRandomElement(capturingPieces);
            List<Move> moves = randomPiece.getPossibleMoves(field);
            return getRandomElement(moves);
        } else {
            List<Move> moves = field.getAllMoves(PieceColor.BLACK);
            return getRandomElement(moves);
        }
    }
    private <T> T getRandomElement(List<T> list) {
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}