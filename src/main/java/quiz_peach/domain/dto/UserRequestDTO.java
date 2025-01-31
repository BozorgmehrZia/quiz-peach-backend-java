package quiz_peach.domain.dto;

import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(@NotNull String name, @NotNull String email, @NotNull String password) {}
