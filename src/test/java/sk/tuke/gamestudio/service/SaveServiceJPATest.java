package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.game.checkers.core.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SaveServiceJPA saveServiceJPA;

    @Test
    void testReset() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        saveServiceJPA.reset();

        verify(query).executeUpdate();
    }

    @Test
    void testAddSave() {
        Save save = new Save("player1", "game1", new Field(), new Date());

        saveServiceJPA.addSave(save);

        verify(entityManager).persist(save);
    }

    @Test
    void testGetSaves() {
        String game = "game1";
        String player = "player1";
        List<Save> saves = new ArrayList<>();
        saves.add(new Save(player, game, new Field(), new Date()));
        saves.add(new Save(player, game, new Field(), new Date()));
        saves.add(new Save(player, game, new Field(), new Date()));

        TypedQuery<Save> query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Save.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(saves);

        List<Save> retrievedSaves = saveServiceJPA.getSaves(game, player);

        assertEquals(saves.size(), retrievedSaves.size());
        for (int i = 0; i < saves.size(); i++) {
            assertEquals(saves.get(i), retrievedSaves.get(i));
        }
    }

}