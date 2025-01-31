package quiz_peach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quiz_peach.domain.entities.AnsweredQuestionUser;
import quiz_peach.domain.entities.AnsweredQuestionUserId;

import java.util.Optional;

public interface AnsweredQuestionUserRepository extends JpaRepository<AnsweredQuestionUser, AnsweredQuestionUserId> {
    Boolean existsByUserIdAndQuestionId(Long userId, Long questionId);

    Optional<AnsweredQuestionUser> findByQuestionIdAndUserId(Long questionId, Long userId);
}