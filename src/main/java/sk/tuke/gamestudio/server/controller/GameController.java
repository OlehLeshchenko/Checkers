package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.checkers.core.*;
import sk.tuke.gamestudio.service.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/checkers")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class GameController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private SaveService saveService;

    private StopWatch stopWatch = new StopWatch();

    private Field field = new Field(true);

    @RequestMapping(value="/capturingpieces", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Piece> getCapturingPieces(){
        return field.getCapturingPieces(field.getCurrentPlayer());
    }


    @RequestMapping(value="/makemove", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody 
    public Field makeMove(@RequestParam(required = false) Integer rowFrom, @RequestParam(required = false) Integer columnFrom, @RequestParam(required = false) Integer rowTo, @RequestParam(required = false) Integer columnTo){
        Tile from = this.field.getTiles()[rowFrom][columnFrom];
        Tile to = this.field.getTiles()[rowTo][columnTo];
        ((Piece) from).getPossibleMoves(field).stream().filter(move1 -> move1.getTo().equals(to)).findFirst().ifPresent(move ->{
            field.makeMove(move);
        });
        if(field.isSolved()) {
            stopWatch.stop();
            field.incrementPlayedTime((int)stopWatch.getTotalTimeSeconds());
        }
        return this.field;
    }

    @RequestMapping(value="/makeBotMove", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field makeBotMove(){
        field.makeBotMove();
        if(field.isSolved()) {
            stopWatch.stop();
            field.incrementPlayedTime((int)stopWatch.getTotalTimeSeconds());
        }
        return this.field;
    }

    @RequestMapping(value="/moves", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Move> getPossibleMoves(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column){
        Tile tile = this.field.getTiles()[row][column];
        if(tile instanceof Piece && field.getCurrentPlayer().equals(((Piece)tile).getColor())){
            List <Piece> capturingPieces= field.getCapturingPieces(PieceColor.WHITE);
            boolean isHavePossibleMoves  = capturingPieces.isEmpty() || capturingPieces.contains((Piece)tile);
            return isHavePossibleMoves ? ((Piece) tile).getPossibleMoves(field) : new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @RequestMapping(value="/field", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field getField(){
        if(!stopWatch.isRunning())stopWatch.start();
        return field;
    }

    @RequestMapping(value="/newgame", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field newGame(@RequestParam(required = false) Boolean bot){
        stopWatch = new StopWatch();
        stopWatch.start();
        field = new Field(true);
        field.getBot().setActive(bot);
        return this.field;
    }

}
