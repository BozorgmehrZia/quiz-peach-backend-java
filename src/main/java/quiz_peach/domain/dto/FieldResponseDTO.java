package quiz_peach.domain.dto;

import quiz_peach.domain.enumeration.FieldType;

public record FieldResponseDTO(
        Long id,
        String name,
        String label,
        FieldType type,
        String defaultValue
) {
}