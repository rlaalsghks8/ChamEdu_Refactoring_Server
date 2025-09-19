package example.com.chamedurefact.web.dto;

import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MentoInfoDto {
    private Long mentoId;
    private Long postId;
    private String name;
    private String email;
    private String university;
    private Major major;
    private AdmissionType admissionType;
    private long ratingAvg;
    private int reviewCount;
    private int menteeCount;
    private String hashTag;
}
