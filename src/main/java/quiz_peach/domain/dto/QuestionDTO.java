package quiz_peach.domain.dto;

import quiz_peach.domain.enumeration.DifficultyLevel;

public record QuestionDTO(
        Long id,
        String name,
        String question,
        String option1,
        String option2,
        String option3,
        String option4,
        DifficultyLevel level,
        Integer answerCount,
        Integer correctAnswerCount,
        String tagName,
        Boolean answered) {
}