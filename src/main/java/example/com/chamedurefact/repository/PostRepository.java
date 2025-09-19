package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post,Long> {
    Post findByUser_Id(Long userId);

    Optional<Post> findById(Long postId);

}
