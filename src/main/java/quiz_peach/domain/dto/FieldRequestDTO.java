package quiz_peach.domain.dto;

public record FieldRequestDTO(
        String name,
        String label,
        FieldType type,
        String defaultValue
) {
}