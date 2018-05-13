package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private MultiplicationResultAttemptRepository attemptRepository;
    private UserRepository userRepository;

    @Autowired
    public MultiplicationServiceImpl(
            final RandomGeneratorService randomGeneratorService,
            final MultiplicationResultAttemptRepository attemptRepository,
            final UserRepository userRepository) {

        this.randomGeneratorService = randomGeneratorService;
        this.attemptRepository = attemptRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Override
    @Transactional
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {

        // Check if the user already exist for that alias
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());

        // Avoids 'hack' attempts
        Assert.isTrue(!attempt.isCorrect(),
                "You can't send an attempt marked as correct!");

        // Checks if it's correct
        boolean isCorrect =
                attempt.getResultAttempt() ==
                        attempt.getMultiplication().getFactorA() * attempt.getMultiplication().getFactorB();

        // Creates a copy, now setting the 'correct' field accordingly
        MultiplicationResultAttempt checkedAttempt =
                new MultiplicationResultAttempt(
                        user.orElse(attempt.getUser()),
                        attempt.getMultiplication(),
                        attempt.getResultAttempt(),
                        isCorrect);

        attemptRepository.save(checkedAttempt);
        return isCorrect;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}
