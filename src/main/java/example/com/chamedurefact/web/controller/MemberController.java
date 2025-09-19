package example.com.chamedurefact.web.controller;

import example.com.chamedurefact.service.MemberService;
import example.com.chamedurefact.web.dto.DefaultResponseDto;
import example.com.chamedurefact.web.dto.LoginResponseDto;
import example.com.chamedurefact.web.dto.UpdateUserProfileRequest;
import example.com.chamedurefact.web.dto.UserProfileDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/kakao")
    @Operation(summary = "카카오 로그인(POST)", description = "프론트가 받은 code를 JSON으로 전달")
    public ResponseEntity<LoginResponseDto> kakaoLogin(@RequestBody String code) {
        log.info("Received Kakao authorization code: {}", code);

        // 1. 인가 코드 -> 액세스 토큰
        String accessToken = memberService.fetchAccessTokenFromKakao(code);

        // 2. 액세스 토큰 -> 사용자 정보
        HashMap<String, Object> userInfo = memberService.fetchKakaoEmailAndName(accessToken);

        // 3. 사용자 정보를 바탕으로 회원가입 및 로그인 처리
        LoginResponseDto response = memberService.login(userInfo);

        // 4. 응답 객체에 프로필 설정 여부와 이메일, 닉네임 추가
        response.setEmail((String) userInfo.get("email"));
        response.setNickname((String) userInfo.get("nickname"));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    @Operation(summary = "프로필 설정", description = "첫 로그인 시 사용자 프로필 정보 등록")
    public ResponseEntity<Void> setupProfile(@RequestBody UserProfileDto profileDto) {
        log.info("Received profile setup request for email: {}", profileDto.getEmail());
        memberService.setupProfile(profileDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<DefaultResponseDto<UserProfileDto>> getMyProfile(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        UserProfileDto profile = memberService.getMyProfile(email);

        DefaultResponseDto<UserProfileDto> resp =
                DefaultResponseDto.<UserProfileDto>builder()
                        .isSuccess(true)
                        .code("2000")
                        .message("프로필 조회 성공")
                        .data(profile)
                        .build();

        return ResponseEntity.ok(resp);
    }

    @PutMapping("/me")
    @Operation(summary = "내 프로필 수정", description = "JWT 토큰의 사용자 기준으로 프로필 수정(이메일은 수정 불가)")
    public ResponseEntity<DefaultResponseDto<UserProfileDto>> updateMyProfile(
            Authentication authentication,
            @RequestBody UpdateUserProfileRequest req) {

        String email = (String) authentication.getPrincipal();
        UserProfileDto updated = memberService.updateMyProfile(email, req);

        return ResponseEntity.ok(
                DefaultResponseDto.<UserProfileDto>builder()
                        .isSuccess(true)
                        .code("2001")
                        .message("프로필 수정 성공")
                        .data(updated)
                        .build()
        );
    }
}