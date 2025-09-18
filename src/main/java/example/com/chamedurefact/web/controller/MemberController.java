package example.com.chamedurefact.web.controller;

import example.com.chamedurefact.service.MemberService;
import example.com.chamedurefact.web.dto.LoginResponseDto;
import example.com.chamedurefact.web.dto.OAuthCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인(POST)", description = "프론트가 받은 code/state를 JSON으로 전달")
    public String kakaoLogin(@RequestBody String code) {
        return memberService.fetchAccessTokenFromKakao(code);
    }
}
