package sk.tuke.gamestudio.game.checkers.core;

import java.util.ArrayList;
import java.util.List;

public class Commoner extends Piece {
    public Commoner(int posX, int posY, PieceColor color) {
        super(posX, posY, color);
    }

    @Override
    public List<Move> getPossibleMoves(Field field) {
        List<Move> possibleMoves = new ArrayList<>();

        int x = getPosX();
        int y = getPosY();
        int yOffset = getColor() == PieceColor.WHITE ? -1 : +1;
        boolean isCapturedMoveDetected = false;

        for (int xOffset = -1; xOffset <= 1; xOffset += 2) {

            Move move = createMoveFromPosition(field, x, y, xOffset, yOffset);
            if (move != null) {
                if (move.isCaptured()) {
                    if (!isCapturedMoveDetected) possibleMoves.clear();
                    possibleMoves.add(move);
                    isCapturedMoveDetected = true;
                } else if (!isCapturedMoveDetected) possibleMoves.add(move);
            }
        }
        return possibleMoves;
    }
}
