package example.com.chamedurefact.web.dto;

import lombok.*;

@Data
@Builder                // 제네릭과 함께 정상 동작합니다
@NoArgsConstructor      // 직렬화에 필요할 수 있어 추가
@AllArgsConstructor
public class DefaultResponseDto<T> {
    private boolean isSuccess;
    private String code;
    private String message;
    private T data;
}
