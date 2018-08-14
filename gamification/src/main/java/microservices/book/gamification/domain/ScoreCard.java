package microservices.book.gamification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class represents the Score linked to an attempt in the game,
 * with an associated user and the timestamp in which the score is
 * registered.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Entity
public final class ScoreCard {

    public static final int DEFAULT_SCORE = 10;

    @Id
    @GeneratedValue
    @Column(name = "card_id")
    private final Long cardId;

    @Column(name = "user_id")
    private final Long userId;

    @Column(name = "attempt_id")
    private final Long attemptId;

    @Column(name = "score_ts")
    private final long scoreTimestamp;

    @Column(name = "score")
    private final int score;

    public ScoreCard() {
        this(null, null,
                null, 0, 0);
    }

    public ScoreCard(Long userId, Long attemptId) {
        this(null, userId, attemptId, System.currentTimeMillis(), DEFAULT_SCORE);
    }
}
