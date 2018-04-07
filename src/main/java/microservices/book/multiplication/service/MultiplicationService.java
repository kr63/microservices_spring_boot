package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;

public interface MultiplicationService {

    /**
     * Creates a {@link Multiplication} object with two randomly generated factors
     *
     * @return a Multiplication object with random factors
     */
    Multiplication createRandomMultiplication();
}
