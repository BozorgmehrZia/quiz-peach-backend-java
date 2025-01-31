package quiz_peach.domain.dto;

public record FilteredQuestionDTO(
    Long id,
    String name,
    Integer level,
    String tag
) {
}
