package sk.tuke.gamestudio.game.checkers.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;

    @BeforeEach
    public void setUp() {
        field = new Field();
        field.clearField();
    }

    @Test
    public void testFieldGenerationCorrectness() {
        field.generate();
        assertEquals(GameState.PLAYING, field.getState());
        assertEquals(PieceColor.WHITE, field.getCurrentPlayer());
        for (int row = 0; row < field.getRowCount(); row++) {
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTiles()[row][column];
                if((row + column) % 2 != 0){
                    if(row < 3){
                        assertInstanceOf(Commoner.class, tile);
                        assertEquals(PieceColor.BLACK, ((Piece)tile).getColor());
                    } else if(row > 4){
                        assertInstanceOf(Commoner.class, tile);
                        assertEquals(PieceColor.WHITE, ((Piece)tile).getColor());
                    }
                    else assertInstanceOf(Tile.class, tile);
                } else assertInstanceOf(Tile.class, tile);
                assertEquals(row, tile.getPosY());
                assertEquals(column, tile.getPosX());
            }
        }
    }

    @Test
    public void testValidMoveExecutionForCommoner() {
        field.generate();
        Tile pieceA3 = field.getTiles()[5][0];
        List<Move> pieceA3Moves = ((Piece) pieceA3).getPossibleMoves(field);
        assertEquals(1, pieceA3Moves.size());
        assertEquals(pieceA3, pieceA3Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[4][1], pieceA3Moves.get(0).getToTile());
        assertFalse(pieceA3Moves.get(0).isCaptured());


        Tile pieceC3 = field.getTiles()[5][2];
        List<Move> pieceC3Moves = ((Piece) pieceC3).getPossibleMoves(field);
        assertEquals(2, pieceC3Moves.size());

        assertEquals(pieceC3, pieceC3Moves.get(0).getFromTile());

        assertEquals(field.getTiles()[4][1], pieceC3Moves.get(0).getToTile());
        assertFalse(pieceC3Moves.get(0).isCaptured());

        assertEquals(field.getTiles()[4][3], pieceC3Moves.get(1).getToTile());
        assertFalse(pieceC3Moves.get(1).isCaptured());

        Tile pieceA1 = field.getTiles()[7][0];
        List<Move> pieceA1Moves = ((Piece) pieceA1).getPossibleMoves(field);
        assertEquals(0, pieceA1Moves.size());
    }

    @Test
    public void testMoveWithCaptureForCommoner() {
        Piece pieceA1 = new Commoner(0, 7, PieceColor.WHITE);
        Piece pieceC1 = new Commoner(2, 7, PieceColor.WHITE);
        Piece pieceB2 = new Commoner(1, 6, PieceColor.BLACK);

        field.setPieceToField(pieceA1);
        field.setPieceToField(pieceC1);
        field.setPieceToField(pieceB2);

        List<Move> pieceA1Moves = pieceA1.getPossibleMoves(field);
        assertEquals(1, pieceA1Moves.size());
        assertTrue(pieceA1Moves.get(0).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[5][2], pieceA1Moves.get(0).getToTile());

        List<Move> pieceC1Moves = pieceC1.getPossibleMoves(field);
        assertEquals(1, pieceC1Moves.size());
        assertTrue(pieceC1Moves.get(0).isCaptured());
        assertEquals(pieceC1, pieceC1Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[5][0], pieceC1Moves.get(0).getToTile());
    }

    @Test
    public void testValidMoveExecutionForKing() {
        field.generate();
        Piece pieceA3 = new King(0,5, PieceColor.WHITE);
        field.setPieceToField(pieceA3);

        List<Move> pieceA3Moves = (pieceA3).getPossibleMoves(field);
        assertEquals(2, pieceA3Moves.size());

        assertEquals(pieceA3, pieceA3Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[4][1], pieceA3Moves.get(0).getToTile());
        assertFalse(pieceA3Moves.get(0).isCaptured());

        assertEquals(pieceA3, pieceA3Moves.get(1).getFromTile());
        assertEquals(field.getTiles()[3][2], pieceA3Moves.get(1).getToTile());
        assertFalse(pieceA3Moves.get(1).isCaptured());

        Piece pieceC3 = new King(2, 5, PieceColor.WHITE);
        field.setPieceToField(pieceC3);
        List<Move> pieceC3Moves = (pieceC3).getPossibleMoves(field);
        assertEquals(4, pieceC3Moves.size());

        assertEquals(pieceC3, pieceC3Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[4][1], pieceC3Moves.get(0).getToTile());
        assertFalse(pieceC3Moves.get(0).isCaptured());

        assertEquals(pieceC3, pieceC3Moves.get(1).getFromTile());
        assertEquals(field.getTiles()[3][0], pieceC3Moves.get(1).getToTile());
        assertFalse(pieceC3Moves.get(1).isCaptured());

        assertEquals(pieceC3, pieceC3Moves.get(2).getFromTile());
        assertEquals(field.getTiles()[4][3], pieceC3Moves.get(2).getToTile());
        assertFalse(pieceC3Moves.get(2).isCaptured());

        assertEquals(pieceC3, pieceC3Moves.get(3).getFromTile());
        assertEquals(field.getTiles()[3][4], pieceC3Moves.get(3).getToTile());
        assertFalse(pieceC3Moves.get(3).isCaptured());
    }

    @Test
    public void testMoveWithCaptureForKing() {
        Piece pieceA1 = new King(0, 7, PieceColor.WHITE);
        Piece pieceC1 = new King(2, 7, PieceColor.WHITE);
        Piece pieceB2 = new Commoner(1, 6, PieceColor.BLACK);

        field.setPieceToField(pieceA1);
        field.setPieceToField(pieceC1);
        field.setPieceToField(pieceB2);

        List<Move> pieceA1Moves = pieceA1.getPossibleMoves(field);
        assertEquals(1, pieceA1Moves.size());
        assertTrue(pieceA1Moves.get(0).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[5][2], pieceA1Moves.get(0).getToTile());

        List<Move> pieceC1Moves = pieceC1.getPossibleMoves(field);
        assertEquals(1, pieceC1Moves.size());
        assertTrue(pieceC1Moves.get(0).isCaptured());
        assertEquals(pieceC1, pieceC1Moves.get(0).getFromTile());
        assertEquals(field.getTiles()[5][0], pieceC1Moves.get(0).getToTile());
    }

}
