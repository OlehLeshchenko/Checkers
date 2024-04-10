package sk.tuke.gamestudio.game.checkers.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        assertEquals(pieceA3, pieceA3Moves.get(0).getFrom());
        assertEquals(field.getTiles()[4][1], pieceA3Moves.get(0).getTo());
        assertFalse(pieceA3Moves.get(0).isCaptured());


        Tile pieceC3 = field.getTiles()[5][2];
        List<Move> pieceC3Moves = ((Piece) pieceC3).getPossibleMoves(field);
        assertEquals(2, pieceC3Moves.size());

        assertEquals(pieceC3, pieceC3Moves.get(0).getFrom());

        assertEquals(field.getTiles()[4][1], pieceC3Moves.get(0).getTo());
        assertFalse(pieceC3Moves.get(0).isCaptured());

        assertEquals(field.getTiles()[4][3], pieceC3Moves.get(1).getTo());
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
        assertEquals(pieceA1, pieceA1Moves.get(0).getFrom());
        assertEquals(field.getTiles()[5][2], pieceA1Moves.get(0).getTo());

        List<Move> pieceC1Moves = pieceC1.getPossibleMoves(field);
        assertEquals(1, pieceC1Moves.size());
        assertTrue(pieceC1Moves.get(0).isCaptured());
        assertEquals(pieceC1, pieceC1Moves.get(0).getFrom());
        assertEquals(field.getTiles()[5][0], pieceC1Moves.get(0).getTo());
    }

    @Test
    public void testValidMoveExecutionForKing() {
        field.generate();
        Piece pieceA3 = new King(0,5, PieceColor.WHITE);
        field.setPieceToField(pieceA3);

        List<Move> pieceA3Moves = (pieceA3).getPossibleMoves(field);
        assertEquals(2, pieceA3Moves.size());

        List<Tile> targetTilesA3 = new ArrayList<>();
        pieceA3Moves.forEach(move -> targetTilesA3.add(move.getTo()));

        assertEquals(pieceA3, pieceA3Moves.get(0).getFrom());
        assertTrue(targetTilesA3.contains(field.getTiles()[4][1]));
        assertFalse(pieceA3Moves.get(0).isCaptured());

        assertEquals(pieceA3, pieceA3Moves.get(1).getFrom());
        assertTrue(targetTilesA3.contains(field.getTiles()[3][2]));
        assertFalse(pieceA3Moves.get(1).isCaptured());

        Piece pieceC3 = new King(2, 5, PieceColor.WHITE);
        field.setPieceToField(pieceC3);
        List<Move> pieceC3Moves = (pieceC3).getPossibleMoves(field);
        assertEquals(4, pieceC3Moves.size());

        List<Tile> targetTilesC3 = new ArrayList<>();
        pieceC3Moves.forEach(move -> targetTilesC3.add(move.getTo()));

        assertEquals(pieceC3, pieceC3Moves.get(0).getFrom());
        assertTrue(targetTilesC3.contains(field.getTiles()[4][1]));
        assertFalse(pieceC3Moves.get(0).isCaptured());

        assertEquals(pieceC3, pieceC3Moves.get(1).getFrom());
        assertTrue(targetTilesC3.contains(field.getTiles()[3][0]));
        assertFalse(pieceC3Moves.get(1).isCaptured());

        assertEquals(pieceC3, pieceC3Moves.get(2).getFrom());
        assertTrue(targetTilesC3.contains(field.getTiles()[4][3]));
        assertFalse(pieceC3Moves.get(2).isCaptured());

        assertEquals(pieceC3, pieceC3Moves.get(3).getFrom());
        assertTrue(targetTilesC3.contains(field.getTiles()[3][4]));
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
        assertEquals(6, pieceA1Moves.size());

        List<Tile> targetTiles = new ArrayList<>();
        pieceA1Moves.forEach(move -> targetTiles.add(move.getTo()));

        assertTrue(pieceA1Moves.get(0).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(0).getFrom());
        assertTrue(targetTiles.contains(field.getTiles()[5][2]));

        assertTrue(pieceA1Moves.get(1).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(1).getFrom());
        assertTrue(targetTiles.contains(field.getTiles()[4][3]));

        assertTrue(pieceA1Moves.get(2).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(2).getFrom());
        assertTrue(targetTiles.contains(field.getTiles()[3][4]));

        assertTrue(pieceA1Moves.get(3).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(3).getFrom());
        assertTrue(targetTiles.contains(field.getTiles()[2][5]));

        assertTrue(pieceA1Moves.get(4).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(4).getFrom());
        assertTrue(targetTiles.contains(field.getTiles()[1][6]));

        assertTrue(pieceA1Moves.get(5).isCaptured());
        assertEquals(pieceA1, pieceA1Moves.get(5).getFrom());
        assertTrue(targetTiles.contains(field.getTiles()[0][7]));

        List<Move> pieceC1Moves = pieceC1.getPossibleMoves(field);
        assertEquals(1, pieceC1Moves.size());
        assertTrue(pieceC1Moves.get(0).isCaptured());
        assertEquals(pieceC1, pieceC1Moves.get(0).getFrom());
        assertEquals(field.getTiles()[5][0], pieceC1Moves.get(0).getTo());
    }

}
