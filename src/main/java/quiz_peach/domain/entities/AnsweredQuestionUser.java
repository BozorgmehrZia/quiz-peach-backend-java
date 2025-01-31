package quiz_peach.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import quiz_peach.domain.enumeration.AnsweredStatus;

@Entity
@Table(name = "tb_answered_question_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AnsweredQuestionUserId.class)
public class AnsweredQuestionUser {

    @Id
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "answered_status", nullable = false)
    private AnsweredStatus answeredStatus;
}

