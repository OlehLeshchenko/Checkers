package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.game.checkers.core.Field;
import sk.tuke.gamestudio.game.checkers.core.GameState;
import sk.tuke.gamestudio.game.checkers.core.PieceColor;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class SaveServiceTest {
    private final SaveService saveService = new SaveServiceJDBC();

//    @Test
    public void testReset() {
        saveService.reset();
        assertEquals(0, saveService.getSaves("checkers", "player").size());
    }

//    @Test
    public void testAddSave() {
        saveService.reset();
        Field save = new Field();
        Date date = new Date();
        save.incrementPlayedTime(1800);
        saveService.addSave(new Save("player", "checkers", save, date));

        var saves = saveService.getSaves("checkers", "player");
        assertEquals(1, saves.size());
        assertEquals("player", saves.get(0).getPlayer());
        assertEquals("checkers", saves.get(0).getGame());
        assertFieldEquals(save, saves.get(0).getSave());
        assertEquals(date, saves.get(0).getSavedOn());
    }

//    @Test
    public void testGetSaves() {
        saveService.reset();
        Field save1 = new Field();

        Field save2 = new Field();
        save2.setCurrentPlayer(PieceColor.BLACK);
        save2.setState(GameState.WHITE_WON);

        Field save3 = new Field();
        save3.generate();

        var date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 2045);
        var lastDate = calendar.getTime();

        save1.incrementPlayedTime(1802);
        save2.incrementPlayedTime(1234);
        save3.incrementPlayedTime(1255);

        saveService.addSave(new Save("player1", "checkers", save1, date));
        saveService.addSave(new Save("player2", "checkers", save2, lastDate));
        saveService.addSave(new Save("player3", "dots", save2, date));
        saveService.addSave(new Save("player4", "checkers", save3, date));

        var saves1 = saveService.getSaves("checkers", "player1");
        var saves2 = saveService.getSaves("checkers", "player2");
        var saves4 = saveService.getSaves("checkers", "player4");

        assertEquals(1, saves1.size());
        assertEquals(1, saves2.size());
        assertEquals(1, saves4.size());

        assertEquals("player1", saves1.get(0).getPlayer());
        assertEquals("checkers", saves1.get(0).getGame());
        assertFieldEquals(save1, saves1.get(0).getSave());
        assertEquals(date, saves1.get(0).getSavedOn());

        assertEquals("player4", saves4.get(0).getPlayer());
        assertEquals("checkers", saves4.get(0).getGame());
        assertFieldEquals(save3, saves4.get(0).getSave());
        assertEquals(date, saves4.get(0).getSavedOn());

        assertEquals("player2", saves2.get(0).getPlayer());
        assertEquals("checkers", saves2.get(0).getGame());
        assertFieldEquals(save2, saves2.get(0).getSave());
        assertEquals(lastDate, saves2.get(0).getSavedOn());
    }

    private void assertFieldEquals(Field field1, Field field2){
        assertArrayEquals(saveService.serialize(field1), saveService.serialize(field2));
    }

}

