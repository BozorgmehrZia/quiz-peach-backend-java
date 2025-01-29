package quiz_peach.domain.dto;

import jakarta.validation.constraints.NotNull;

public record AnswerQuestionRequestDTO(
    @NotNull Long questionId,
    @NotNull Integer option
) {
}
