package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.tuke.gamestudio.entity.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceJPATest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CommentServiceJPA commentServiceJPA;

    @Test
    public void testReset() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        commentServiceJPA.reset();

        verify(query).executeUpdate();
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment("player1", "game1", "Great game!", new Date());

        commentServiceJPA.addComment(comment);

        verify(entityManager).persist(comment);
    }

    @Test
    public void testGetComments() {
        String game = "game1";
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("player1", game, "Great game!", new Date()));
        comments.add(new Comment("player2", game, "Awesome!", new Date()));

        TypedQuery<Comment> query = mock(TypedQuery.class);
        when(entityManager.createQuery(any(String.class), eq(Comment.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(comments);

        List<Comment> retrievedComments = commentServiceJPA.getComments(game);

        assertEquals(comments.size(), retrievedComments.size());
        for (int i = 0; i < comments.size(); i++) {
            assertEquals(comments.get(i), retrievedComments.get(i));
        }
    }

}
