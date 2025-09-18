package example.com.chamedurefact.web.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthCodeRequest {
    private String code;
    private String state;
}
