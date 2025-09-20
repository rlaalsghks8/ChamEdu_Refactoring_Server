package example.com.chamedurefact.web.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    private String nickname;
    private String university;
    private String major;
    private String recruitmentType;
}
