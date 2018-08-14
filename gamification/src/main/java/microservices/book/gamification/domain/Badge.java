package microservices.book.gamification.domain;

/**
 * Enumeration with the different types of Badges that a user can win
 */
public enum Badge {

    // depend on score
    BRONZE_MULTIPLICATOR,
    SILVER_MULTIPLICATOR,
    GOLD_MULTIPLICATOR,

    // other badges
    FIRST_ATTEMPT,
    FIRST_WON
}
