package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.tuke.gamestudio.entity.Score;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScoreServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ScoreServiceJPA scoreServiceJPA;

    @Test
    void testReset() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        scoreServiceJPA.reset();

        verify(query).executeUpdate();
    }

    @Test
    void testAddScore() {
        Score score = new Score("player1", "game1", 100, new Date());

        scoreServiceJPA.addScore(score);

        verify(entityManager).persist(score);
    }

    @Test
    void testGetTopScores() {
        String game = "game1";
        List<Score> scores = new ArrayList<>();
        scores.add(new Score("player1", game, 100, new Date()));
        scores.add(new Score("player2", game, 90, new Date()));
        scores.add(new Score("player3", game, 80, new Date()));
        TypedQuery<Score> query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Score.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(scores);

        List<Score> retrievedScores = scoreServiceJPA.getTopScores(game);

        assertEquals(scores.size(), retrievedScores.size());
        for (int i = 0; i < scores.size(); i++) {
            assertEquals(scores.get(i), retrievedScores.get(i));
        }
    }

}