package example.com.chamedurefact.web.dto;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import example.com.chamedurefact.domain.enums.RecruitmentType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class MainPageResponseDto {

    private List<MentoInfoDto> popular;
    private RecruitmentType recruitmentType;
    private List<MentoInfoDto> recommendByAdmissionType;
    private Major major;
    private List<MentoInfoDto> recommendByMajor;


}
