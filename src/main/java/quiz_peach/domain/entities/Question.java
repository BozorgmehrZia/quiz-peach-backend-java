package quiz_peach.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import quiz_peach.domain.enumeration.DifficultyLevel;

import java.util.List;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option1;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option2;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option3;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String option4;

    @Column(name = "correct_option", nullable = false)
    private Integer correctOption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel level = DifficultyLevel.EASY;

    @Column(name = "answer_count", nullable = false)
    private Integer answerCount = 0;

    @Column(name = "correct_answer_count", nullable = false)
    private Integer correctAnswerCount = 0;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnsweredQuestionUser> answeredQuestions;

    @ManyToMany
    @JoinTable(
            name = "related_questions",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "related_id")
    )
    private List<Question> relatedQuestions;
}

