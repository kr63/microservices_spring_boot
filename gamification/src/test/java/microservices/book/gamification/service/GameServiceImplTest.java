package microservices.book.gamification.service;

import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

public class GameServiceImplTest {

    private GameServiceImpl gameService;

    @Mock
    private ScoreCardRepository scoreCardRepository;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @Mock
    private MultiplicationResultAttemptClient multiplicationClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gameService = new GameServiceImpl(
                scoreCardRepository, badgeCardRepository, multiplicationClient);

        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe",
                20, 70,
                1400, true);

        given(multiplicationClient.retrieveMultiplicationResultAttemptById(anyLong()))
                .willReturn(attempt);
    }

    @Test
    public void processFirstCorrectAttemptTest() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.emptyList());

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // than
        assertThat(iteration.getScore()).isEqualTo(scoreCard.getScore());
        assertThat(iteration.getBadges()).containsOnly(Badge.FIRST_WON);
    }

    @Test
    public void processCorrectAttemptForLuckyNumberBadgeTest() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 10;
        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(1, userId));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                "john_doe", 42, 10,
                420, true);
        given(multiplicationClient.retrieveMultiplicationResultAttemptById(attemptId))
                .willReturn(attempt);

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // than
        assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(iteration.getBadges()).containsOnly(Badge.LUCKY_NUMBER);
    }

    private List<ScoreCard> createNScoreCards(int n, Long userId) {
        return IntStream.range(0, n)
                .mapToObj(i -> new ScoreCard(userId, (long) i))
                .collect(Collectors.toList());
    }

    @Test
    public void processCorrectAttemptForScoreBadgeTest() {
        // given
        Long userId = 1L;
        Long attemptId = 29L;
        int totalScore = 100;
        BadgeCard firstWonBadge = new BadgeCard(userId, Badge.FIRST_WON);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(createNScoreCards(10, userId));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(firstWonBadge));

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, true);

        // than
        assertThat(iteration.getScore()).isEqualTo(ScoreCard.DEFAULT_SCORE);
        assertThat(iteration.getBadges()).containsOnly(Badge.BRONZE_MULTIPLICATOR);
    }

    @Test
    public void processWrongAttemptTest() {
        // given
        Long userId = 1L;
        Long attemptId = 8L;
        int totalScore = 10;
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
                .willReturn(Collections.singletonList(scoreCard));
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.emptyList());

        // when
        GameStats iteration = gameService.newAttemptForUser(userId, attemptId, false);

        // than
        assertThat(iteration.getScore()).isEqualTo(0);
        assertThat(iteration.getBadges()).isEmpty();
    }

    @Test
    public void retrieveStatsForUserTest() {
        // given
        Long userId = 1L;
        int totalScore = 1000;
        BadgeCard badgeCard = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
        given(scoreCardRepository.getTotalScoreForUser(userId))
                .willReturn(totalScore);
        given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
                .willReturn(Collections.singletonList(badgeCard));

        // when
        GameStats stats = gameService.retrieveStatsForUser(userId);

        // than
        assertThat(stats.getScore()).isEqualTo(totalScore);
        assertThat(stats.getBadges()).containsOnly(Badge.SILVER_MULTIPLICATOR);
    }
}