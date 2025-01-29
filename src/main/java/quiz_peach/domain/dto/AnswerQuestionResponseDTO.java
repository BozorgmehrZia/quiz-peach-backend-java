package quiz_peach.domain.dto;


public record AnswerQuestionResponseDTO(
    Boolean isCorrect,
    String message
) {
}
