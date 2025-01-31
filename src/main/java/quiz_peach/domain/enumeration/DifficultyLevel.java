package quiz_peach.domain.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DifficultyLevel {
    EASY(1, "آسان"),
    MEDIUM(2, "متوسط"),
    HARD(3, "سخت");

    private final Integer id;
    private final String name;

    public static DifficultyLevel getDifficultyLevelById(Integer id) {
        return DifficultyLevel.values()[id - 1];
    }
}
