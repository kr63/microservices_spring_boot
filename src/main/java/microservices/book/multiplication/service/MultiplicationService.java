package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {

    /**
     * Creates a random {@link Multiplication} object
     *
     * @return a multiplication of randomly generated numbers
     */
    Multiplication createRandomMultiplication();

    /**
     * @return true if the attemp matches the result of the multiplication, false otherwise
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);
}
