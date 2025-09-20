package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.User;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import example.com.chamedurefact.domain.enums.RecruitmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    boolean existsByNickname(String nickname);

    @Query("SELECT u FROM User u JOIN u.posts p WHERE p.id = :postId")
    User findByPostId(@Param("postId") Long postId);


    @Query(value = "SELECT u.* " +
            "FROM user u " +
            "JOIN review r ON u.id = r.user_id " +
            "WHERE EXISTS (SELECT 1 FROM post p WHERE p.user_id = u.id) " +
            "GROUP BY u.id " +
            "ORDER BY AVG(r.review_score) DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<User> findTop5ByAvgReviewScoreWithPosts();

    @Query(value = "SELECT u.* " +
            "FROM User u " +
            "JOIN Post p ON u.id = p.user_id " +
            "LEFT JOIN Review r ON u.id = r.user_id " +
            "WHERE u.admission_type = :admissionType " +
            "GROUP BY u.id " +
            "ORDER BY COALESCE(AVG(r.review_score), 0) DESC " +
            "LIMIT 5",
            nativeQuery = true)
    List<User> findTop5ByAdmissionTypeOrderByAvgReviewScoreAndHasPost(@Param("admissionType") RecruitmentType recruitmentType);

    @Query(value = "SELECT u.* " +
            "FROM User u " +
            "JOIN Post p ON u.id = p.user_id " +
            "JOIN Review r ON u.id = r.user_id " +
            "WHERE u.major = :major " +
                    "GROUP BY u.id " +
                    "ORDER BY AVG(r.review_score) DESC " +
                    "LIMIT 5",
            nativeQuery = true)
    List<User> findTop5ByMajorOrderByAvgReviewScoreAndHasPost(@Param("major") Major major);


    @Query(value = "SELECT DISTINCT u.* " +
            "FROM User u " +
            "JOIN Post p ON u.id = p.user_id"+
            "ORDER BY u.createdAt DESC"
            ,
            nativeQuery = true)
    Page<User> findAllUsersWithPosts(Pageable pageable);  //전체 멘토 포스트 찾기

    @Query(value = "SELECT u.* " +
            "FROM User u " +
            "JOIN Post p ON u.id = p.user_id " +
            "LEFT JOIN Review r ON u.id = r.user_id " +  // LEFT JOIN으로 변경
            "GROUP BY u.id " +
            "ORDER BY COALESCE(AVG(r.review_score), 0) DESC",  // 리뷰 없는 경우 0으로 처리
            nativeQuery = true)
    Page<User> findAllUsersWithPostOrderByReviewScoreDesc(Pageable pageable);  //리뷰 점수 순


    @Query(value = "SELECT u.* " +
            "FROM User u " +
            "JOIN Post p ON u.id = p.user_id " +
            "JOIN ChatUser cu ON u.id = cu.user_id " +
            "GROUP BY u.id " +
                    "ORDER BY COUNT(cu.chat_id) DESC",
            nativeQuery = true)
    Page<User> findTopMentorsWithPosts(Pageable pageable);



    

}
