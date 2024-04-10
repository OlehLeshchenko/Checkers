package sk.tuke.gamestudio.game.checkers.core;

import com.google.common.annotations.VisibleForTesting;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@EqualsAndHashCode
public class Field implements Serializable {
    @Getter
    private final int rowCount = 8;
    @Getter
    private final int columnCount = 8;
    @Getter
    private final Tile[][] tiles = new Tile[rowCount][columnCount];
    @Getter
    @EqualsAndHashCode.Exclude
    private final Bot bot = new Bot();
    @Setter
    @Getter
    @EqualsAndHashCode.Exclude
    private GameState state;
    @EqualsAndHashCode.Exclude
    private Piece pieceWhichCanMakeSecondMove = null;
    @Setter
    @Getter
    private PieceColor currentPlayer;
    @Getter
    private int playedTime = 0;

    public Field() {
    }

    public void incrementPlayedTime(int additionalTime) {
        this.playedTime += additionalTime;
    }

    public boolean isSolved() {
        return state != GameState.PLAYING;
    }

    public void generate() {
        setState(GameState.PLAYING);
        setCurrentPlayer(PieceColor.WHITE);
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                PieceColor color = (row < 3) ? PieceColor.BLACK : PieceColor.WHITE;
                if ((row == 0 || row == 6 || row == 2) && column % 2 == 1) {
                    tiles[row][column] = new Commoner(column, row, color);
                } else if ((row == 1 || row == 7 || row == 5) && column % 2 == 0) {
                    tiles[row][column] = new Commoner(column, row, color);
                } else {
                    tiles[row][column] = new Tile(column, row);
                }
            }
        }
    }

    public void makeMove(Move move) {
        Piece from = move.getFrom();
        Tile to = move.getTo();

        if (from.equals(pieceWhichCanMakeSecondMove)) pieceWhichCanMakeSecondMove = null;

        removePieceFromTile(from);
        if (move.isCaptured()) removeCapturedPiece(from, to);
        from = generatePieceAtPosition(from, to.getPosX(), to.getPosY());

        tiles[to.getPosY()][to.getPosX()] = from;

        checkGameState();
        if (move.isCaptured() && canMakeSecondMove(from)){
            pieceWhichCanMakeSecondMove = from;
            bot.makeMove(this);
        }
        else setCurrentPlayer(getCurrentPlayer() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
    }

    private Piece generatePieceAtPosition(Piece tileFrom, int posX, int posY) {
        if (tileFrom instanceof Commoner) {
            if ((tileFrom.getColor() == PieceColor.WHITE && posY == 0) || (tileFrom.getColor() == PieceColor.BLACK && posY == getRowCount() - 1)) {
                tileFrom = new King(posX, posY, tileFrom.getColor());
            } else tileFrom = new Commoner(posX, posY, tileFrom.getColor());
        } else {
            tileFrom = new King(posX, posY, tileFrom.getColor());
        }
        return tileFrom;
    }

    private void removeCapturedPiece(Piece from, Tile to) {
        int xFrom = from.getPosX();
        int yFrom = from.getPosY();

        int yDirection = to.getPosY() > yFrom ? 1 : -1;
        int xDirection = to.getPosX() > xFrom ? 1 : -1;

        do {
            xFrom += xDirection;
            yFrom += yDirection;

        } while (!(tiles[yFrom][xFrom] instanceof Piece));

        removePieceFromTile((Piece) tiles[yFrom][xFrom]);
    }

    private void removePieceFromTile(Piece tile) {
        tiles[tile.getPosY()][tile.getPosX()] = new Tile(tile.getPosX(), tile.getPosY());
    }

    private void checkGameState() {
        PieceColor teamToCheck = getCurrentPlayer() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        if (isMoveImpossible(teamToCheck)){
            GameState newState = teamToCheck == PieceColor.BLACK ? GameState.WHITE_WON : GameState.BLACK_WON;
            setState(newState);
        }
    }

    private boolean isMoveImpossible(PieceColor team) {
        return getAllMoves(team).isEmpty();
    }

    public List<Piece> getCapturingPieces(PieceColor team) {
        List<Piece> capturingPieces = new ArrayList<>();

        if (pieceWhichCanMakeSecondMove == null) {
            getAllMoves(team).forEach(move -> {
                if (move.isCaptured()) capturingPieces.add(move.getFrom());
            });
        } else capturingPieces.add(pieceWhichCanMakeSecondMove);

        return new ArrayList<>(new HashSet<>(capturingPieces));
    }

    public List<Move> getAllMoves(PieceColor team) {
        List<Move> moves = new ArrayList<>();
        Arrays.stream(tiles).flatMap(Arrays::stream).filter(tile -> tile instanceof Piece && ((Piece) tile).getColor().equals(team)).forEach(tile -> moves.addAll(((Piece) tile).getPossibleMoves(this)));
        return moves;
    }

    private boolean canMakeSecondMove(Piece piece) {
        return piece.getPossibleMoves(this).stream().anyMatch(Move::isCaptured);
    }

    public CurrentPlayerScores getScores() {
        List<Piece> playerPieces = Arrays.stream(getTiles()).flatMap(Arrays::stream).filter(tile -> tile instanceof Piece && ((Piece) tile).getColor().equals(PieceColor.WHITE)).map(tile -> (Piece) tile).toList();

        int commoners = playerPieces.stream().filter(piece -> piece instanceof Commoner).toList().size();
        int kings = playerPieces.stream().filter(piece -> piece instanceof King).toList().size();
        int minutes = Math.floorDiv(getPlayedTime(), 60);

        return new CurrentPlayerScores(commoners, kings, minutes);
    }

    public boolean isOpponentPiece(Piece piece) {
        return piece.getColor() != currentPlayer;
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < getColumnCount() && y >= 0 && y < getRowCount();
    }

    public void makeBotMove() {
        bot.makeMove(this);
    }

    @VisibleForTesting
    void clearField() {
        setState(GameState.PLAYING);
        setCurrentPlayer(PieceColor.WHITE);
        for (int row = 0; row < getRowCount(); row++) {
            for (int column = 0; column < getColumnCount(); column++) {
                tiles[row][column] = new Tile(column, row);
            }
        }
    }

    @VisibleForTesting
    void setPieceToField(Piece piece) {
        tiles[piece.getPosY()][piece.getPosX()] = piece;
    }
}