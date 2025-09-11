package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    Post findByUser_Id(Long userId);
}
