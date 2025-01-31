package quiz_peach.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateQuestionDTO(
    @NotNull String name,
    @NotNull String question,
    @NotNull String option1,
    @NotNull String option2,
    @NotNull String option3,
    @NotNull String option4,
    @NotNull @JsonProperty("correct_option") Integer correctOption,
    @NotNull Integer level,
    @NotNull @JsonProperty("tag_name") String tagName,
    @JsonProperty("related_ids") List<Long> relatedIds
) {
}
