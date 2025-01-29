package quiz_peach.domain.dto;

import quiz_peach.domain.enumeration.DifficultyLevel;

public record FilteredQuestionDTO(
    Long id,
    String name,
    DifficultyLevel level,
    String tag
) {
}
