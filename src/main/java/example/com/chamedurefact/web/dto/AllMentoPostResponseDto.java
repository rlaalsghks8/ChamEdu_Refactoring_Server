package example.com.chamedurefact.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class AllMentoPostResponseDto {
    private List<MentoInfoDto> allMentoList;
}
