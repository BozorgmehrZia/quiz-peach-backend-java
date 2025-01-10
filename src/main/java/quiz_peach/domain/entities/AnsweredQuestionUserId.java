package quiz_peach.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnsweredQuestionUserId implements Serializable {
    private Long question;
    private Long user;
}
