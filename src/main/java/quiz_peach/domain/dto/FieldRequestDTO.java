package quiz_peach.domain.dto;

import quiz_peach.domain.enumeration.FieldType;

public record FieldRequestDTO(
        String name,
        String label,
        FieldType type,
        String defaultValue
) {
}