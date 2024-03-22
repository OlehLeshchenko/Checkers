package sk.tuke.gamestudio.game.checkers.core;

import java.util.List;

public abstract class Piece extends Tile {

    private final PieceColor color;

    public Piece(int posX, int posY, PieceColor color) {
        super(posX, posY);
        this.color = color;
    }

    public PieceColor getColor() {
        return color;
    }

    public List<Move> getPossibleMoves(Field field) {
        return null;
    }

    protected Move createMoveFromPosition(Field field, int x, int y, int xOffset, int yOffset) {
        int newX = x + xOffset;
        int newY = y + yOffset;

        if (field.isWithinBounds(newX, newY)) {

            Tile tile = field.getTiles()[newY][newX];
            if (!(tile instanceof Piece)) {
                return new Move(this, tile);
            } else if (canCaptureOpponentPiece(field, xOffset, yOffset, newX, newY, (Piece) tile)) {
                Tile newTile = field.getTiles()[newY + offsetDirection(yOffset)][newX + offsetDirection(xOffset)];
                if (!(newTile instanceof Piece)) {
                    Move move = new Move(this, newTile);
                    move.setCaptured(true);
                    return move;
                }
            }
        }
        return null;
    }

    private boolean canCaptureOpponentPiece(Field field, int xOffset, int yOffset, int newX, int newY, Piece tile) {
        return field.isWithinBounds(newX + offsetDirection(xOffset), newY + offsetDirection(yOffset)) && field.isOpponentPiece(tile);
    }

    private int offsetDirection(int offset) {
        return offset > 0 ? 1 : -1;
    }
}
