package sk.tuke.gamestudio.server.webservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RatingServiceRestTest {
    @Mock
    private RatingService ratingService;
    @InjectMocks
    private RatingServiceRest ratingServiceRest;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ratingServiceRest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void setRatingTest() throws Exception {
        Rating rating = new Rating("player1", "checkers", 5, new Date());
        String ratingJson = objectMapper.writeValueAsString(rating);
        mockMvc.perform(post("/api/rating")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ratingJson))
                .andDo(print())
                .andExpect(status().isOk());

        verify(ratingService, times(1)).setRating(rating);
    }

    @Test
    void getRatingTest() throws Exception {
        String game = "checkers";
        String player = "player1";
        when(ratingService.getRating(game, player)).thenReturn(4);
        mockMvc.perform(get("/api/rating/{game}/{player}", game, player))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4));

        verify(ratingService, times(1)).getRating(game, player);
    }

    @Test
    void getAverageRatingTest() throws Exception {
        String game = "checkers";
        when(ratingService.getAverageRating(game)).thenReturn(5);
        mockMvc.perform(get("/api/rating/{game}", game))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));

        verify(ratingService, times(1)).getAverageRating(game);
    }
}