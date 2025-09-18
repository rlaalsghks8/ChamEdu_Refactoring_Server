package example.com.chamedurefact.web.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private boolean newUser;
}
