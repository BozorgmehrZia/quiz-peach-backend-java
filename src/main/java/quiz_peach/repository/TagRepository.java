package quiz_peach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quiz_peach.domain.entities.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    @Query("SELECT t FROM Tag t WHERE (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) ORDER BY " +
           "CASE WHEN :isDesc = true THEN t.questionNumber END DESC, " +
           "CASE WHEN :isDesc = false THEN t.questionNumber END ASC")
    List<Tag> findByNameContainingIgnoreCaseOrderByQuestionNumber(String name, boolean desc);

    Boolean existsByName(String name);
}