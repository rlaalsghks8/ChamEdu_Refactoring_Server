package example.com.chamedurefact.web.dto;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import lombok.Data;

@Data
public class PostRequestDto {
    private String title;
    private String content;
    private String hashTag;
    private AdmissionType admissionType;
    private Major major;
}
