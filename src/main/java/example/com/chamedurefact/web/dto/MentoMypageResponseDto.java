package example.com.chamedurefact.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class MentoMypageResponseDto {

    private String backGroundImg;
    private PostInfoDto postInfo;
    private List<ReviewResponseDto> reviewList;
}
