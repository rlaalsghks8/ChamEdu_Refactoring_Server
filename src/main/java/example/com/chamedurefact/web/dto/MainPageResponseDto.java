package example.com.chamedurefact.web.dto;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MainPageResponseDto {

    private List<MentoInfoDto> popular;
    private AdmissionType admissionType;
    private List<MentoInfoDto> recommendByAdmissionType;
    private Major major;
    private List<MentoInfoDto> recommendByMajor;


}
