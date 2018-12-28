package microservices.book.gamification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ScoreController.class)
public class ScoreControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<ScoreCard> json;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getScoreForAttemptTest() throws Exception {
        // given
        ScoreCard scoreCard = new ScoreCard(
                1L, 5L, 10L, System.currentTimeMillis(), 100);
        String jsonExpect = json.write(scoreCard).getJson();
        given(gameService.getScoreForAttempt(10L)).willReturn(scoreCard);

        // when & then
        mvc.perform(get("/scores/10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonExpect));
    }
}