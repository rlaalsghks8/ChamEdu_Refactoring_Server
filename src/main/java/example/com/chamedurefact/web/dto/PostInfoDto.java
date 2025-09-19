package example.com.chamedurefact.web.dto;

import lombok.Data;

@Data
public class PostInfoDto {
    private Long postId;
    private String title;
    private String content;
    private String hashTag;
}
