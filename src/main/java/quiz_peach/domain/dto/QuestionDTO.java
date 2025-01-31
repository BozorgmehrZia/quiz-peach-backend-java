package quiz_peach.domain.dto;

public record QuestionDTO(
        Long id,
        String name,
        String question,
        String option1,
        String option2,
        String option3,
        String option4,
        Integer level,
        Integer answerCount,
        Integer correctAnswerCount,
        String tagName,
        Boolean answered) {
}