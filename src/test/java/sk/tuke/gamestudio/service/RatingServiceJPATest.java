package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RatingServiceJPA ratingServiceJPA;

    @Test
    public void testReset() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        ratingServiceJPA.reset();

        verify(query).executeUpdate();
    }

    @Test
    public void testSetRating() {
        Rating rating = new Rating("player1", "checkers", 4, new Date());
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        doReturn(query).when(query).setParameter(anyInt(), any());

        ratingServiceJPA.setRating(rating);

        verify(entityManager).createNativeQuery(anyString());
        verify(query, atLeastOnce()).setParameter(anyInt(), any());
    }

    @Test
    public void testGetRating() {
        String game = "checkers";
        String player = "player1";
        int expectedRating = 4;
        TypedQuery<Integer> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Integer.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedRating);

        int actualRating = ratingServiceJPA.getRating(game, player);

        assertEquals(expectedRating, actualRating);
    }

    @Test
    public void testGetAverageRating() {
        String game = "checkers";
        double expectedAverageRating = 4.0;
        TypedQuery<Double> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Double.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedAverageRating);

        int actualAverageRating = ratingServiceJPA.getAverageRating(game);

        assertEquals((int) expectedAverageRating, actualAverageRating);
    }

}
