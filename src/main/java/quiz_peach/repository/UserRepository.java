package quiz_peach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quiz_peach.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}