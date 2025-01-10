package quiz_peach.domain.dto;

public record FormResponseDTO(
        Long id,
        String name,
        boolean published,
        String submitUrl
) {
}