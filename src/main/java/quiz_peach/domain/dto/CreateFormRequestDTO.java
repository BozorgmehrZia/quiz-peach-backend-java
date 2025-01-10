package quiz_peach.domain.dto;

import java.util.List;

public record CreateFormRequestDTO(
        String name,
        boolean published,
        List<FieldRequestDTO> fields,
        String submitUrl
) {
}