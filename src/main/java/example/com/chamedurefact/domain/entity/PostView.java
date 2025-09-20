package example.com.chamedurefact.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "post_view",
        uniqueConstraints = @UniqueConstraint(columnNames = {"viewer_id", "post_id"}),
        indexes = {
                @Index(name = "idx_post_view_viewer_lastviewed", columnList = "viewer_id,last_viewed_at DESC")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PostView extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 최근 본 "사람" (멘티)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viewer_id", nullable = false)
    private User viewer;

    // 최근 본 "게시글"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "last_viewed_at", nullable = false)
    private LocalDateTime lastViewedAt;

    @Column(name = "view_count", nullable = false)
    private long viewCount;

    public void touch() {
        this.lastViewedAt = LocalDateTime.now();
        this.viewCount++;
    }
}
