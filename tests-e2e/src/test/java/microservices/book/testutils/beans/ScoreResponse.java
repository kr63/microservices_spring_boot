package microservices.book.testutils.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScoreResponse {
    private long userId;
    private int score;

    public ScoreResponse(final int score) {
        this.score = score;
    }
}
