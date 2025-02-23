package quiz_peach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quiz_peach.domain.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByNameOrEmail(String name, String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND u.id <> :currentUserId ORDER BY " +
            "CASE WHEN :isDesc = true THEN u.score END DESC, " +
            "CASE WHEN :isDesc = false THEN u.score END ASC")
    List<User> findByNameContainingIgnoreCaseAndIdNotOrderByScore(String name, Long currentUserId, boolean isDesc);

    @Query("SELECT u FROM User u WHERE u.name = :name")
    Optional<User> findByName(String name);

    @Query("SELECT u FROM User u JOIN u.followedUsers f WHERE f.id = :userId")
    List<User> findFollowedUsers(Long userId);
}
