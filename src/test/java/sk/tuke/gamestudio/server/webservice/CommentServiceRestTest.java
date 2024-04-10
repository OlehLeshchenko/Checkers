package sk.tuke.gamestudio.server.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentServiceRestTest {
    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentServiceRest commentServiceRest;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentServiceRest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addCommentTest() throws Exception {
        Comment comment = new Comment();
        String commentJson = objectMapper.writeValueAsString(comment);
        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(commentService, times(1)).addComment(comment);
    }

    @Test
    void getCommentsTest() throws Exception {
        String game = "checkers";
        Date date = new Date();

        List<Comment> comments = new ArrayList<>(Arrays.asList(
            new Comment("player1", "checkers", "Good game!", date),
            new Comment("player2", "checkers", "The best game ever!!!", date),
            new Comment("player4", "checkers", "good game", date)
        ));

        when(commentService.getComments(game)).thenReturn(comments);
        mockMvc.perform(get("/api/comment/{game}", game))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].player").value(Matchers.containsInRelativeOrder("player1", "player2", "player4")))
                .andExpect(jsonPath("$[*].game", Matchers.hasItems(game)))
                .andExpect(jsonPath("$[*].comment").value(Matchers.containsInRelativeOrder("Good game!", "The best game ever!!!", "good game")))
                .andExpect(jsonPath("$[*].commentedOn", Matchers.everyItem(Matchers.equalTo(date.getTime()))));

        verify(commentService, times(1)).getComments(game);
    }
}