package microservices.book.testutils.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttemptResponse {
    private boolean correct;
    private long id;
    private User user;
}
