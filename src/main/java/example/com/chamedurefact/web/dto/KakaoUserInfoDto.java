package example.com.chamedurefact.web.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class KakaoUserInfoDto {
    private String email;
    private String nickname;
}
