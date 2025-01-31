package quiz_peach.domain.dto;


public record AnswerQuestionResponseDTO(
    Boolean correct,
    String message
) {
}
