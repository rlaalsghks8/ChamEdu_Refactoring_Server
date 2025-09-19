package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.User;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query(value = "SELECT u.* " +
            "FROM User u " +
            "JOIN Review r ON u.id = r.user_id " +
            "GROUP BY u.id " +
            "ORDER BY AVG(r.review_score) DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<User> findTop5ByAvgReviewScore();

    @Query(value = "SELECT u.* " +
            "FROM User u " +
            "JOIN Review r ON u.id = r.user_id " +
            "WHERE u.admission_type = :admissionType " +
            "GROUP BY u.id " +
            "ORDER BY AVG(r.review_score) DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<User> findTop5ByAdmissionTypeOrderByAvgReviewScore(AdmissionType admissionType);

    @Query(value="SELECT u.*"+
    "FROM User u" +
    "JOIN Review r ON u.id = r.user_id"+
    "WHERE u.major =:major"+
    "GROUP BY u.id"+
    "ORDER BY AVG(r.review_score) DESC" +
    "LIMIT 5",
    nativeQuery = true)
    List<User> findTop5ByMajorOrderByAvgReviewScore(Major major);

}
