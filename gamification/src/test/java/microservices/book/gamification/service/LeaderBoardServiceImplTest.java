package microservices.book.gamification.service;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class LeaderBoardServiceImplTest {

    private LeaderBoardServiceImpl leaderBoardService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        leaderBoardService = new LeaderBoardServiceImpl(scoreCardRepository);
    }

    @Test
    public void retrieveLeaderBoardTest() {
        // given
        Long userId = 1L;
        LeaderBoardRow leaderBoardRow = new LeaderBoardRow(userId, 300L);
        List<LeaderBoardRow> expectedLeaderBoard = Collections.singletonList(leaderBoardRow);
        given(scoreCardRepository.findFirst10()).willReturn(expectedLeaderBoard);

        // when
        List<LeaderBoardRow> leaderBoard = leaderBoardService.getCurrentLeaderBoard();

        // then
        assertThat(leaderBoard).isEqualTo(expectedLeaderBoard);
    }
}