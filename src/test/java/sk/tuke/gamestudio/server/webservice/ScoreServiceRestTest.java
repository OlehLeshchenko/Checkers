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
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;

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
public class ScoreServiceRestTest {
    @Mock
    private ScoreService scoreService;
    @InjectMocks
    private ScoreServiceRest scoreServiceRest;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scoreServiceRest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addScoreTest() throws Exception {
        Score score = new Score("player1", "checkers", 150, new Date());
        String scoreJson = objectMapper.writeValueAsString(score);
        mockMvc.perform(post("/api/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(scoreJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(scoreService, times(1)).addScore(score);
    }


    @Test
    void getTopScoresTest() throws Exception {
        String game = "checkers";
        Date date = new Date();
        List<Score> scores = new ArrayList<>(Arrays.asList(
                new Score("player2", game, 150, date),
                new Score("player1", game, 120, date),
                new Score("player4", game, 100, date)
        ));

        when(scoreService.getTopScores("checkers")).thenReturn(scores);
        mockMvc.perform(get("/api/score/{game}", game))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].player").value(Matchers.containsInRelativeOrder("player2", "player1", "player4")))
                .andExpect(jsonPath("$[*].game", Matchers.hasItems(game)))
                .andExpect(jsonPath("$[*].points").value(Matchers.containsInRelativeOrder(150, 120, 100)))
                .andExpect(jsonPath("$[*].playedOn", Matchers.everyItem(Matchers.equalTo(date.getTime()))));

        verify(scoreService, times(1)).getTopScores(game);
    }

}