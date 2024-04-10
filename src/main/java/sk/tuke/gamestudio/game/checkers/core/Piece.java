package sk.tuke.gamestudio.game.checkers.core;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class Piece extends Tile {

    private final PieceColor color;

    public Piece(int posX, int posY, PieceColor color) {
        super(posX, posY);
        this.color = color;
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
                Move move = new Move(this, tile);


                int column = x + offsetDirection(xOffset);
                for(int row = y + offsetDirection(yOffset); !(row == newY && column == newX); row+=offsetDirection(yOffset), column+=offsetDirection(xOffset)){
                    Tile tempTile = field.getTiles()[row][column];
                    PieceColor color  = field.getCurrentPlayer() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
                    if(tempTile instanceof Piece && ((Piece)tempTile).getColor().equals(color)){
                        move.setCaptured(true);
                    }
                }
                return move;
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
