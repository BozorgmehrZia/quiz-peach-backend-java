package quiz_peach.domain.dto;

import jakarta.validation.constraints.NotNull;
import quiz_peach.domain.enumeration.DifficultyLevel;

import java.util.List;

public record CreateQuestionDTO(
    @NotNull String name,
    @NotNull String question,
    @NotNull String option1,
    @NotNull String option2,
    @NotNull String option3,
    @NotNull String option4,
    @NotNull Integer correctOption,
    @NotNull DifficultyLevel level,
    @NotNull String tagName,
    List<Long> relatedIds
) {
}
