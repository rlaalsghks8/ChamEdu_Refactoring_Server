package example.com.chamedurefact.web.dto;
import lombok.*;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfileDto {
    private String email;
    private boolean isMentor;
    private String nickname;
    private String university;
    private String major;
    private String recruitmentType;
}
