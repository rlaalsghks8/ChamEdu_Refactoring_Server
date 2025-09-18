package example.com.chamedurefact.web.controller;

import example.com.chamedurefact.service.MemberService;
import example.com.chamedurefact.web.dto.LoginResponseDto;
import example.com.chamedurefact.web.dto.OAuthCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인(POST)", description = "프론트가 받은 code를 JSON으로 전달")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestBody String code) {
        log.info("Received Kakao authorization code: {}", code);

        // 1. 인가 코드를 이용해 액세스 토큰을 받아옵니다.
        String accessToken = memberService.fetchAccessTokenFromKakao(code);

        // 2. 액세스 토큰을 이용해 사용자 정보를 받아옵니다.
        HashMap<String, Object> userInfo = memberService.fetchKakaoEmailAndName(accessToken);

        // 3. 사용자 정보를 응답에 담아 반환합니다.
        return ResponseEntity.ok(userInfo);
    }
}