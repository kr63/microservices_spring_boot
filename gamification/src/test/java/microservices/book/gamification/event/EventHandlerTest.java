package microservices.book.gamification.event;

import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class EventHandlerTest {

    private EventHandler eventHandler;

    @Mock
    private GameService gameService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventHandler = new EventHandler(gameService);
    }

    @Test
    public void multiplicationAttemptReceivedTest() {
        // given
        Long userId = 1L;
        Long attempId = 8L;
        GameStats gameStatsExpected = new GameStats();
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(attempId, userId, true);
        given(gameService.newAttemptForUser(userId, attempId, true)).willReturn(gameStatsExpected);

        // when
        eventHandler.handleMultiplicationSolved(event);

        // then
        verify(gameService).newAttemptForUser(userId, attempId, true);
    }
}