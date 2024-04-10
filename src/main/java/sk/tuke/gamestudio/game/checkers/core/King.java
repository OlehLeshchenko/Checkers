package sk.tuke.gamestudio.game.checkers.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class King extends Piece {
    public King(int posX, int posY, PieceColor color) {
        super(posX, posY, color);
    }

    @Override
    public List<Move> getPossibleMoves(Field field) {
        List<Move> possibleMoves = new ArrayList<>();

        int x = getPosX();
        int y = getPosY();
        boolean isCapturedMoveDetected = false;

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] direction : directions) {
            int xOffset = direction[0];
            int yOffset = direction[1];

            for (int i = 1; i <= field.getColumnCount(); i++) {
                Move move = createMoveFromPosition(field, x, y, i * xOffset, i * yOffset);
                if (move != null) {
                    if (move.isCaptured()) {
                        if (!isCapturedMoveDetected) possibleMoves.clear();
                        possibleMoves.add(move);
                        isCapturedMoveDetected = true;
                    } else if (!isCapturedMoveDetected) possibleMoves.add(move);
                } else break;
            }
        }
        return new ArrayList<>(new HashSet<>(possibleMoves));
    }

}
