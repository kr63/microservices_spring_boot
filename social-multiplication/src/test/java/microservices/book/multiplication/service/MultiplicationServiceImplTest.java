package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventDispatcher eventDispatcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(
                randomGeneratorService, attemptRepository, userRepository, eventDispatcher);
    }

    @Test
    public void createRandomMultiplicationTest() {
        // given
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        // when
        Multiplication multiplication =
                multiplicationServiceImpl.createRandomMultiplication();

        // assert
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_dow");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                user, multiplication, 3000, false);

        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(
                user, multiplication, 3000, true);

        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
                attempt.getId(), attempt.getUser().getId(), true);

        given(userRepository.findByAlias("john_dow")).willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        // assert
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void checkWrongAttemptTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_dow");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                user, multiplication, 3010, false);
        MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
                attempt.getId(), attempt.getUser().getId(), false);
        given(userRepository.findByAlias("john_dow")).willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationServiceImpl.checkAttempt(attempt);

        // assert
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
        verify(eventDispatcher).send(eq(event));
    }

    @Test
    public void retrieveStatsTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("john_dow");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(
                user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(
                user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);

        given(userRepository.findByAlias("john_dow")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("john_dow")).willReturn(latestAttempts);

        // when
        List<MultiplicationResultAttempt> latestAttemptsResult =
                multiplicationServiceImpl.getStatsForUser("john_dow");

        // then
        assertThat(latestAttemptsResult).isEqualTo(latestAttempts);
    }
}