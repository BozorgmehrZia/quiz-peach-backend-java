package quiz_peach.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record AnswerQuestionRequestDTO(
    @NotNull @JsonProperty("question_id") Long questionId,
    @NotNull Integer option
) {
}
