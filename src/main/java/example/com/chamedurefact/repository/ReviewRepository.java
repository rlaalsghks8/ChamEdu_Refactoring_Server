package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
