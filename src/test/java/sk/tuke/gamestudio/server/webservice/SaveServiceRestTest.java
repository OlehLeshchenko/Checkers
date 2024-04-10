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
import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.game.checkers.core.Field;
import sk.tuke.gamestudio.service.SaveService;

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
class SaveServiceRestTest {
    @Mock
    private SaveService saveService;
    @InjectMocks
    private SaveServiceRest saveServiceRest;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(saveServiceRest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addSaveTest() throws Exception {
        Save save = new Save("player1", "checkers", new Field(), new Date());
        String saveJson = objectMapper.writeValueAsString(save);
        mockMvc.perform(post("/api/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(saveJson))
                .andDo(print())
                .andExpect(status().isOk());
        verify(saveService, times(1)).addSave(save);
    }

    @Test
    void getSavesTest() throws Exception {
        String game = "checkers";
        String player = "player1";
        Field field = new Field();
        Date date = new Date();
        List<Save> saves = new ArrayList<>(Arrays.asList(
                new Save(player, game, field, date),
                new Save(player, game, field, date),
                new Save(player, game, field, date)
        ));

        when(saveService.getSaves(game, player)).thenReturn(saves);
        mockMvc.perform(get("/api/save/{game}/{player}", game, player))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].player", Matchers.hasItems(player)))
                .andExpect(jsonPath("$[*].game", Matchers.hasItems(game)))
                .andExpect(jsonPath("$[*].save").exists())
                .andExpect(jsonPath("$[*].savedOn", Matchers.everyItem(Matchers.equalTo(date.getTime()))));

        verify(saveService, times(1)).getSaves(game, player);
    }
}