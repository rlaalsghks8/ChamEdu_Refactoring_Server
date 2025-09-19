package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    int countByUser_Id(Long userId);



    @Query("SELECT AVG(r.reviewScore) FROM Review r WHERE r.user.id = :userId")
    Long findAverageReviewScoreByUserId(@Param("userId") Long userId);

    List<Review> findAllByUser_Id(Long userId);

    List<Review> findAllByPost_Id(Long postId);


}
