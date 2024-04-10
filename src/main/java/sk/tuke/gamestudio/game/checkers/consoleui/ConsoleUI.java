package sk.tuke.gamestudio.game.checkers.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.game.checkers.core.*;
import sk.tuke.gamestudio.service.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private SaveService saveService;

    private static final String GAME = "checkers";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private static final Pattern TILE_PATTERN = Pattern.compile("([a-hA-H])\\s*([1-8])");
    private static final Pattern RULES_PATTERN = Pattern.compile("(R|RULES)");
    private static final Pattern EXIT_PATTERN = Pattern.compile("(X|EXIT)");
    private static final Pattern HELP_PATTERN = Pattern.compile("(H|HELP)");
    private static final Pattern YES_NO_PATTERN = Pattern.compile("([NY]|YES|NO)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("([0-9]+)");

    private final Scanner scanner = new Scanner(System.in);
    private final List<Tile> possibleTilesToMove = new ArrayList<>();
    private final StopWatch stopWatch = new StopWatch();
    private Field field;
    private String player;

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {
        field.generate();
        System.out.print("\n\u001B[1;34m              --- Welcome to the game of Checkers! ---\n\n" + ANSI_RESET);
        printGameRating();
        printTopScores();
        if (!login()) setGameMode();
        printListOfCommands();
        stopWatch.start();
        do {
            show();
            processInput();
        } while (!field.isSolved());

        show();
        stopWatch.stop();
        field.incrementPlayedTime((int) stopWatch.getTotalTimeSeconds());
        switch (field.getState()) {
            case WHITE_WON -> {
                printWhiteVictory();
                if (field.getBot().isActive()) printGameScore();
            }
            case BLACK_WON -> printBlackVictory();
        }
        rateGame();
        leaveComment();
        printLastComments();
        createNewGame();
    }

    private boolean login() {
        final Pattern NICKNAME_PATTERN = Pattern.compile("([a-zA-z0-9]+)");
        String line;
        do {
            System.out.print("Please enter your nickname: ");
            line = scanner.nextLine().trim();
        } while (!NICKNAME_PATTERN.matcher(line).matches());

        System.out.print("\nHello, " + line + "!\n\n");

        setPlayer(line);
        List<Save> playerSaves = saveService.getSaves(GAME, player);

        if (!playerSaves.isEmpty()) {

            if (yesNoInput("Do you want to load your previous game(Y/N): ")) {
                for (int i = 0; i < playerSaves.size(); i++) {
                    System.out.printf("Save %d: %s\n", i + 1, playerSaves.get(i).getSavedOn());
                }

                do {
                    System.out.print("Choose the number of save you want to load: ");
                    line = scanner.nextLine().trim().toUpperCase();
                } while (!NUMBER_PATTERN.matcher(line).matches() || Integer.parseInt(line) < 0 || Integer.parseInt(line) > playerSaves.size());
                System.out.println();

                setField(playerSaves.get(Integer.parseInt(line) - 1).getSave());
                return true;
            }
        }
        return false;
    }

    private void setGameMode() {
        boolean validInput = false;

        System.out.println("Choose a game mode!");

        while (!validInput) {
            System.out.print("Please enter 'S' for Single Player or 'T' for Two Players: ");
            var line = scanner.nextLine().trim().toUpperCase();
            if (line.equals("S")) {
                field.getBot().setActive(true);
                validInput = true;
            } else if (line.equals("T")) {
                System.out.println(ANSI_YELLOW + "\nNo points will be awarded when playing in Two Players mode!!!\n" + ANSI_RESET);
                field.getBot().setActive(false);
                validInput = true;
            } else {
                System.out.println(ANSI_RED + "Invalid input!" + ANSI_RESET);
            }
        }
        System.out.println();
    }

    private void setField(Field field) {
        this.field = field;
    }

    private void setPlayer(String player) {
        this.player = player;
    }

    private boolean yesNoInput(String text) {
        String line;
        do {
            System.out.print(text);
            line = scanner.nextLine().trim().toUpperCase();
        } while (!YES_NO_PATTERN.matcher(line).matches());

        return line.equals("YES") || line.equals("Y");
    }

    private void rateGame() {
        if (yesNoInput("Do you want to rate the game? (Y/N): ")) {
            String line;
            System.out.println("Rate the game from 1 to 5.");
            do {
                System.out.print("Game Rating: ");
                line = scanner.nextLine().trim().toUpperCase();
            } while (!NUMBER_PATTERN.matcher(line).matches() || Integer.parseInt(line) < 1 || Integer.parseInt(line) > 5);
            ratingService.setRating(new Rating(player, GAME, Integer.parseInt(line), new Date()));
            System.out.println();
        }
    }

    private void leaveComment() {
        if (yesNoInput("Do you want to leave a comment? (Y/N): ")) {
            String line;
            System.out.println("The maximum number of characters is 250.");
            System.out.print("Comment: ");
            line = scanner.nextLine().trim();
            if (line.length() > 250) line = line.substring(0, 250);
            commentService.addComment(new Comment(player, GAME, line, new Date()));
            System.out.println();
        }
    }

    private void createNewGame() {
        if (yesNoInput("Do you want to play the game again? (Y/N): ")) {
            setField(new Field());
            play();
        } else System.out.println(ANSI_YELLOW + "See you next time! We hope you'll play another game!" + ANSI_RESET);
    }

    private void printGameScore() {
        final String ANSI_YELLOW_BOLD = "\u001B[1;33m";
        System.out.println("----------------------------------------------------------------------");
        System.out.print("\n\u001B[1;34m                         --- Your Score! ---\n\n" + ANSI_RESET);
        CurrentPlayerScores scores = field.getScores();

        int total = scores.getCommonerPoints() + scores.getKingPoints() + scores.getTimePoints();

        scoreService.addScore(new Score(player, GAME, total, new Date()));

        System.out.println("Amount of commoners: " + scores.getCommonersCount());
        System.out.println(ANSI_YELLOW_BOLD + "                                                  " + scores.getCommonerPoints() + ANSI_RESET);

        System.out.println("Amount of kings: " + scores.getKingsCount());
        System.out.println(ANSI_YELLOW_BOLD + "                                                  " + scores.getKingPoints() + ANSI_RESET);

        System.out.println("Amount of played minutes: " + scores.getMinutes());
        System.out.println(ANSI_YELLOW_BOLD + "                                                  " + scores.getTimePoints() + ANSI_RESET);
        System.out.println("\n\u001B[1;34mScore:        " + ANSI_RESET + ANSI_YELLOW_BOLD + total + ANSI_RESET + "\n");
    }

    private void processInput() {
        List<Piece> capturingPieces = field.getCapturingPieces(field.getCurrentPlayer());
        printCaptureMessage(capturingPieces);

        System.out.print("Enter the command: ");

        String line = scanner.nextLine().trim().toUpperCase();
        Tile from = getTile(TILE_PATTERN.matcher(line));

        System.out.println();
        if (from != null) processMove(from, capturingPieces);
        else if (RULES_PATTERN.matcher(line).matches()) printRules();
        else if (EXIT_PATTERN.matcher(line).matches()) {
            stopWatch.stop();
            field.incrementPlayedTime((int) stopWatch.getTotalTimeSeconds());
            saveService.addSave(new Save(player, GAME, field, new Date()));
            System.out.println(ANSI_YELLOW + "This game will be saved!" + ANSI_RESET);
            System.exit(0);
        } else if (HELP_PATTERN.matcher(line).matches()) printListOfCommands();
        else System.out.println(ANSI_RED + "Bad input!" + ANSI_RESET);

    }

    private void processMove(Tile from, List<Piece> capturingPieces) {
        String line;
        if (!(from instanceof Piece)) {
            System.out.println(ANSI_RED + "There is no piece on the selected tile!" + ANSI_RESET);
            return;
        }

        List<Move> possibleMoves = getPossibleMovesWithChecking(from, capturingPieces);
        if (possibleMoves == null) return;

        showPossibleMoves(possibleMoves);

        System.out.print("Enter which tile you want to move to: ");
        line = scanner.nextLine().trim().toUpperCase();
        Tile to = getTile(TILE_PATTERN.matcher(line));
        System.out.println();

        Move move = possibleMoves.stream().filter(item -> item.getTo().equals(to)).findFirst().orElse(null);

        if (move != null) {
            field.makeMove(move);
            if (!field.getBot().isActive())
                System.out.println("Change team to " + (field.getCurrentPlayer() != PieceColor.WHITE ? "Black" : "White"));
            else if (field.getCurrentPlayer().equals(PieceColor.BLACK)) {
                show();
                System.out.println("\n----------------------------------\n--- The bot has made its move! ---\n----------------------------------\n");
                field.makeBotMove();
            }
        } else if (!EXIT_PATTERN.matcher(line).matches())
            System.out.println(ANSI_RED + "This piece cannot go to the selected tile!" + ANSI_RESET);
    }

    private Tile getTile(Matcher matcher) {
        if (matcher.matches()) {
            int rowFrom = 7 - (Integer.parseInt(matcher.group(2)) - 1);
            int columnFrom = matcher.group(1).charAt(0) - 'A';
            return field.getTiles()[rowFrom][columnFrom];
        } else return null;
    }

    private List<Move> getPossibleMovesWithChecking(Tile tileFrom, List<Piece> capturingPieces) {
        if (!(((Piece) tileFrom).getColor() == field.getCurrentPlayer())) {
            System.out.println(ANSI_RED + "The selected piece is the wrong color!" + ANSI_RESET);
            return null;
        }
        if (!capturingPieces.isEmpty() && !capturingPieces.contains(tileFrom)) {
            System.out.println(ANSI_RED + "You must move with a piece that can capture a piece!" + ANSI_RESET);
            return null;
        }

        List<Move> possibleMoves = ((Piece) tileFrom).getPossibleMoves(field);
        if (possibleMoves.isEmpty()) {
            System.out.println(ANSI_RED + "There are no available moves for the selected piece!" + ANSI_RESET);
            return null;
        }
        return possibleMoves;
    }

    private void show() {
        printFieldBody();
        printColumnLegends();
        System.out.println();
    }

    private void showPossibleMoves(List<Move> possibleMoves) {
        possibleMoves.forEach(move -> possibleTilesToMove.add(move.getTo()));
        show();
        printPossibleMoves(possibleMoves);
        possibleTilesToMove.clear();
    }

    private void printFieldBody() {
        Tile[][] tiles = field.getTiles();
        for (int row = 0; row < 8; row++) {
            System.out.print(ANSI_BLUE + (8 - row) + ANSI_RESET + " ");
            for (int column = 0; column < 8; column++)
                printTile(tiles[row][column]);
            System.out.println();
        }
    }

    private void printColumnLegends() {
        System.out.print("  ");
        for (int i = 0; i < field.getColumnCount(); i++)
            System.out.print(ANSI_BLUE + " " + (char) ('a' + i) + " "  + ANSI_RESET);
    }

    private void printTile(Tile tile) {
        String symbol;
        if (tile instanceof Piece piece) {
            if (piece.getColor() == PieceColor.BLACK) symbol = (piece instanceof King) ? "◎" : "○";
            else symbol = (piece instanceof King) ? "◉" : "●";
        } else if (possibleTilesToMove.contains(tile)) symbol = ANSI_YELLOW + "◊";
        else symbol = " ";

        if ((tile.getPosY() + tile.getPosX()) % 2 == 0) {
            String ANSI_WHITE_BG = "\u001b[47m";
            System.out.print(ANSI_WHITE_BG + " " + symbol + " " + ANSI_RESET);
        } else System.out.print( "\u001B[40m" + " " + symbol + " " + ANSI_RESET);
    }

    private void printCaptureMessage(List<Piece> capturingPieces) {
        if (!capturingPieces.isEmpty()) {
            System.out.print("You can only make a move with pieces that can capture your opponent's piece: ");
            printTilesCoordinates(capturingPieces);
        }
    }

    private void printPossibleMoves(List<Move> possibleMoves) {
        System.out.print("Available moves: ");
        printTilesCoordinates(possibleMoves.stream().map(Move::getTo).toList());
    }

    private void printTilesCoordinates(List<? extends Tile> targetTiles) {
        List<String> coordinates = new ArrayList<>();
        targetTiles.forEach(piece -> coordinates.add(String.valueOf((char) (piece.getPosX() + 'A')) + (8 - piece.getPosY())));
        Collections.sort(coordinates);
        coordinates.forEach(coordinate -> System.out.print(ANSI_GREEN + coordinate + ANSI_RESET + " "));
        System.out.println();
    }

    private void printListOfCommands() {
        System.out.println("----------------------------------------------------------------------");
        System.out.println("\u001B[1;33mList of available commands:\n" + ANSI_RESET + "RULES/R - Display game rules\n" + "<COORDINATES> - Enter the coordinates of the piece you want to move (e.g., A1, B2)\n" + "EXIT/X - Exit the game\n" + "HELP/H - Display this list of commands");
        System.out.println("----------------------------------------------------------------------");
    }

    private void printRules() {
        final String ANSI_YELLOW_BOLD = "\u001B[1;33m";
        final String ANSI_GREEN_BOLD = "\u001B[1;32m";

        System.out.println("--------------------------------------------------------------------------------------------------------------------------");
        System.out.println(ANSI_YELLOW_BOLD + "Rules\n\n" + ANSI_RESET + ANSI_YELLOW_BOLD + "Purpose: " + ANSI_RESET + "The goal of a game of checkers is to capture all of your opponent's pieces or \n" + "block them so that they cannot make any more moves." + " And also to earn as many points \n" + "as possible.\n" + "\n" + ANSI_YELLOW_BOLD + "Movement: " + ANSI_RESET + "The pieces can only move diagonally forward, one tile at a time, to a free tile. \n" + "Kings (after reaching the opponent's back row) can also move across all tiles on the diagonal, \n" + "as well as diagonally backwards.\n" + "\n" + ANSI_YELLOW_BOLD + "Capture: " + ANSI_RESET + "If a player's piece is adjacent to an opponent's piece and there is an empty tile \n" + "behind it diagonally, the player must jump over the opponent's piece, capturing it. If multiple \n" + "jumps are possible, they must be made in series.\n" + "\n" + ANSI_YELLOW_BOLD + "Coronation: " + ANSI_RESET + "When a player's piece reaches the opponent's back row, it is crowned as a \"king\". \n" + "Kings can move and capture pieces diagonally both forward and backward.\n" + "\n" + ANSI_YELLOW_BOLD + "Double Move: " + ANSI_RESET + "If a player's piece makes a capture that opens up the possibility of another capture \n" + "in the same turn, the player must continue to make consecutive captures as long as such opportunities\n" + "exist. In the event that, after capturing the first piece, there are several options for further captures,\n" + "the player has the right to choose which piece he wants to hit.\n" + "\n" + ANSI_YELLOW_BOLD + "End of the game: " + ANSI_RESET + "The game ends when one of the players captures all of the opponent's pieces or when one of\n" + "the players cannot make any of the allowed moves.\n" + "\n" + ANSI_GREEN_BOLD + "Score\n" + ANSI_RESET + "\n" + ANSI_GREEN_BOLD + "Regular Pieces: " + ANSI_RESET + "Each regular piece remaining in a player's possession at the end \n" + "of the game is worth 75 points.\n" + "\n" + ANSI_GREEN_BOLD + "Kings: " + ANSI_RESET + "A piece that has turned into a queen (passed across the board and reached the opposite edge) \n" + "is worth 150 points. Each subsequent king is worth twice as many points as the previous one.\n" + "\n" + ANSI_GREEN_BOLD + "Initial allocation of points: " + ANSI_RESET + "Each player starts the game with 10,000 points.\n" + "\n" + ANSI_GREEN_BOLD + "Deducting points for time: " + ANSI_RESET + "With each minute of play, a player loses 250 points.");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------\n");
    }

    private void printTopScores() {
        List<Score> scores = scoreService.getTopScores(GAME);

        System.out.println("----------------------------------------------------------------------");
        System.out.println("                            Top Scores!!!");
        for (int i = 0; i < scores.size(); i++)
            System.out.println((i + 1) + ": " + scores.get(i).getPlayer() + " " + scores.get(i).getPoints());
        System.out.println("----------------------------------------------------------------------");
    }

    private void printLastComments() {
        List<Comment> comments = commentService.getComments(GAME);

        System.out.println("----------------------------------------------------------------------");
        System.out.println("                            Last Comments!!!");
        comments.forEach(comment -> {
            System.out.println(comment.getPlayer() + ":");
            System.out.println(comment.getComment() + "\n");
        });
        System.out.println("----------------------------------------------------------------------");
    }

    private void printGameRating() {
        int gameRating = ratingService.getAverageRating(GAME);

        System.out.print("----------------------------------------------------------------------");
        System.out.println("\n\u001B[1;34mGame Rating: " + ANSI_RESET + ANSI_YELLOW + gameRating + " / 5" + ANSI_RESET);
        System.out.println("----------------------------------------------------------------------");
    }

    private void printBlackVictory() {
        System.out.println("\u001B[1;37m  ____    _                  _                                   _ \n" + " |  _ \\  | |                | |                                 | |\n" + " | |_) | | |   __ _    ___  | | __   __      __   ___    _ __   | |\n" + " |  _ <  | |  / _` |  / __| | |/ /   \\ \\ /\\ / /  / _ \\  | '_ \\  | |\n" + " | |_) | | | | (_| | | (__  |   <     \\ V  V /  | (_) | | | | | |_|\n" + " |____/  |_|  \\__,_|  \\___| |_|\\_\\     \\_/\\_/    \\___/  |_| |_| (_)\n" + ANSI_RESET);
    }

    private void printWhiteVictory() {
        System.out.println("\u001B[1;37m __          __  _       _   _                                         _ \n" + " \\ \\        / / | |     (_) | |                                       | |\n" + "  \\ \\  /\\  / /  | |__    _  | |_    ___    __      __   ___    _ __   | |\n" + "   \\ \\/  \\/ /   | '_ \\  | | | __|  / _ \\   \\ \\ /\\ / /  / _ \\  | '_ \\  | |\n" + "    \\  /\\  /    | | | | | | | |_  |  __/    \\ V  V /  | (_) | | | | | |_|\n" + "     \\/  \\/     |_| |_| |_|  \\__|  \\___|     \\_/\\_/    \\___/  |_| |_| (_)\n" + ANSI_RESET);
    }

}