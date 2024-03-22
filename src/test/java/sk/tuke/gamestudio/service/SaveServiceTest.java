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

    @Test
    public void testReset() {
        saveService.reset();
        assertEquals(0, saveService.getSaves("checkers").size());
    }

    @Test
    public void testAddSave() {
        saveService.reset();
        Field save = new Field();
        Date date = new Date();
        saveService.addSave(new Save("player", "checkers", save, date, 1800));

        var saves = saveService.getSaves("checkers");
        assertEquals(1, saves.size());
        assertEquals("player", saves.get(0).getPlayer());
        assertEquals("checkers", saves.get(0).getGame());
        assertFieldEquals(save, saves.get(0).getSave());
        assertEquals(date, saves.get(0).getSavedAt());
        assertEquals(1800, saves.get(0).getPlayedTime());
    }

    @Test
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

        saveService.addSave(new Save("player1", "checkers", save1, date, 1802));
        saveService.addSave(new Save("player2", "checkers", save2, lastDate, 1234));
        saveService.addSave(new Save("player3", "dots", save2, date, 567));
        saveService.addSave(new Save("player4", "checkers", save3, date, 1255));

        var saves = saveService.getSaves("checkers");

        assertEquals(3, saves.size());

        assertEquals("player1", saves.get(0).getPlayer());
        assertEquals("checkers", saves.get(0).getGame());
        assertFieldEquals(save1, saves.get(0).getSave());
        assertEquals(date, saves.get(0).getSavedAt());
        assertEquals(1802, saves.get(0).getPlayedTime());

        assertEquals("player4", saves.get(1).getPlayer());
        assertEquals("checkers", saves.get(1).getGame());
        assertFieldEquals(save3, saves.get(1).getSave());
        assertEquals(date, saves.get(1).getSavedAt());
        assertEquals(1255, saves.get(1).getPlayedTime());

        assertEquals("player2", saves.get(2).getPlayer());
        assertEquals("checkers", saves.get(2).getGame());
        assertFieldEquals(save2, saves.get(2).getSave());
        assertEquals(lastDate, saves.get(2).getSavedAt());
        assertEquals(1234, saves.get(2).getPlayedTime());
    }

    private void assertFieldEquals(Field field1, Field field2){
        assertArrayEquals(saveService.serialize(field1), saveService.serialize(field2));
    }

}

