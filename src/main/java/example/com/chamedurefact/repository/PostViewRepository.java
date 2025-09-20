// PostViewRepository.java
package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.PostView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface PostViewRepository extends JpaRepository<PostView, Long> {

    Optional<PostView> findByViewer_IdAndPost_Id(Long viewerId, Long postId);

    @Query("""
        select pv
        from PostView pv
        join fetch pv.post p
        join fetch p.user u
        where pv.viewer.id = :viewerId
        order by pv.lastViewedAt desc
    """)
    List<PostView> findRecentByViewerId(Long viewerId, Pageable pageable);
}
