package quiz_peach.domain.entities;

import quiz_peach.domain.enumeration.FieldType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String label;

    @Enumerated(EnumType.STRING)
    private FieldType type;

    private String defaultValue;
}
