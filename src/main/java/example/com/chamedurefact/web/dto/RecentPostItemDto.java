// RecentPostItemDto.java
package example.com.chamedurefact.web.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentPostItemDto {
    private Long postId;
    private String title;
    private String postImage;
    private String hashTag;
    private String authorNickname;    // p.user.nickname
    private LocalDateTime lastViewedAt;
}
