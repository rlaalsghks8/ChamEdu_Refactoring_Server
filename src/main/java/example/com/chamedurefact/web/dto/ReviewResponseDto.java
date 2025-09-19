package example.com.chamedurefact.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private Long reviewScore;
    private LocalDateTime createdAt;
    private String content;
}
