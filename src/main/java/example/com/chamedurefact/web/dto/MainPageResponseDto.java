package example.com.chamedurefact.web.dto;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainPageResponseDto {


    private AdmissionType admissionType;
    private Major major;

}
