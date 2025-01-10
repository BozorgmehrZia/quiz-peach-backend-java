package quiz_peach.domain.dto;

public record UpdateFormRequestDTO(
        String name,
        boolean published,
        String submitUrl
) {
}