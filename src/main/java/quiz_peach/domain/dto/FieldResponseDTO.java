package quiz_peach.domain.dto;

public record FieldResponseDTO(
        Long id,
        String name,
        String label,
        FieldType type,
        String defaultValue
) {
}