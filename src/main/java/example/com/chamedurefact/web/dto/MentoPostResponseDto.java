package example.com.chamedurefact.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class MentoPostResponseDto {
    private MentoInfoDto mentoInfoDto;
    private List<ReviewResponseDto> reviewList;
}
