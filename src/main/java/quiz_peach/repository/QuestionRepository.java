package quiz_peach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quiz_peach.domain.entities.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCreator_Id(Long id);
    @Query("SELECT q FROM Question q " +
           "WHERE (:name IS NULL OR q.name LIKE %:name%) " +
           "AND (:level IS NULL OR q.level = :level)")
    List<Question> findByFilters(String name, String level);

}