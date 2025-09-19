package example.com.chamedurefact.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.com.chamedurefact.domain.entity.User;
import example.com.chamedurefact.domain.enums.Major;
import example.com.chamedurefact.domain.enums.RecruitmentType;
import example.com.chamedurefact.jwt.JwtTokenProvider;
import example.com.chamedurefact.repository.UserRepository;
import example.com.chamedurefact.web.dto.KakaoTokenResponseDto;
import example.com.chamedurefact.web.dto.LoginResponseDto;
import example.com.chamedurefact.web.dto.UpdateUserProfileRequest;
import example.com.chamedurefact.web.dto.UserProfileDto;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.*;

@Slf4j
@NoArgsConstructor
@Service
public class MemberService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret:}")
    private String clientSecret;

    @Value("${kakao.redirect-url}")
    private String redirectUri;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    //클라이언트 인가 코드 -> 카카오 access token
    public String fetchAccessTokenFromKakao(String code) {
        //카카오 토큰 교환 엔드포인트
        String url = "https://kauth.kakao.com/oauth/token";

        //요청 헤더 생성, 전송 형태 명시
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //카카오에서 요구하는 필수 값들
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }

        //RestTemplate로 폼+헤더를 묶어 POST로 전송
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(form, headers), String.class);

        //실패 처리
        if (!res.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("카카오 토큰 교환 실패: " + res.getStatusCode());
        }

        //응답 Body를 파싱, 최상위 필드의 access token을 꺼냄
        try {
            ObjectMapper mapper = new ObjectMapper();
            KakaoTokenResponseDto dto = mapper.readValue(
                    java.util.Objects.requireNonNull(res.getBody()),
                    KakaoTokenResponseDto.class
            );

            String accessToken = (dto != null) ? dto.getAccessToken() : null;
            if (accessToken == null || accessToken.isBlank()) {
                throw new IllegalStateException("카카오 access_token 없음");
            }
            // 출력으로 확인
            System.out.println("액세스 토큰: " + accessToken);
            return accessToken;

        } catch (Exception e) {
            throw new RuntimeException("카카오 토큰 파싱 실패", e);
        }
    }

    //access token으로 사용자 정보 받아오기
    public HashMap<String, Object> fetchKakaoEmailAndName(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);                    // Authorization: Bearer {token}
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );

        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
            throw new IllegalStateException("카카오 사용자 정보 조회 실패: " + res.getStatusCode());
        }

        try {
            JsonNode root = objectMapper.readTree(res.getBody());

            String email = null;
            JsonNode kakaoAccount = root.path("kakao_account");
            if (kakaoAccount.isObject()) {
                JsonNode emailNode = kakaoAccount.path("email");
                if (emailNode.isTextual()) email = emailNode.asText();
            }

            String nickname = null;
            if (kakaoAccount.isObject()) {
                JsonNode profile = kakaoAccount.path("profile");
                if (profile.isObject()) {
                    JsonNode nickNode = profile.path("nickname");
                    if (nickNode.isTextual()) nickname = nickNode.asText();
                }
            }
            if (nickname == null) {
                JsonNode properties = root.path("properties");
                if (properties.isObject()) {
                    JsonNode nickNode2 = properties.path("nickname");
                    if (nickNode2.isTextual()) nickname = nickNode2.asText();
                }
            }

            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", email);
            userInfo.put("nickname", nickname);

            log.info("Kakao user email={}, nickname={}",
                    email != null ? email : "(null)",
                    nickname != null ? nickname : "(null)");

            return userInfo;

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }

    //로그인
    public LoginResponseDto login(HashMap<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("nickname");

        Optional<User> userOptional = userRepository.findByEmail(email);
        LoginResponseDto response = new LoginResponseDto();

        response.setEmail(email);
        response.setNickname(name);
        response.setToken(jwtTokenProvider.createToken(email));

        if (userOptional.isPresent()) {
            //기존 회원
            User user = userOptional.get();
            response.setNewUser(!user.getIsProfileSetup());
        } else {
            //신규 회원 (회원가입)
            User newUser = User.builder()
                    .email(email)
                    .realName(name)
                    .isProfileSetup(false)
                    .build();
            userRepository.save(newUser);
            response.setNewUser(true);
        }

        return response;
    }

    //신규 회원 프로필 설정
    public void setupProfile(UserProfileDto profileDto) {
        User user = userRepository.findByEmail(profileDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setNickname(profileDto.getNickname());
        user.setMentor(profileDto.isMentor());
        user.setUniversity(profileDto.getUniversity());
        user.setMajor(Major.fromKoreanName(profileDto.getMajor()));
        user.setRecruitmentType(RecruitmentType.fromKoreanName(profileDto.getRecruitmentType()));
        user.setIsProfileSetup(true);

        userRepository.save(user);
    }

    //프로필 조회
    public UserProfileDto getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return UserProfileDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .university(user.getUniversity())
                .major(user.getMajor() != null ? user.getMajor().name() : null)
                .recruitmentType(user.getRecruitmentType() != null ? user.getRecruitmentType().name() : null)
                .isMentor(user.isMentor())
                .build();
    }

    //프로필 수정 (email, mentor 제외)
    @Transactional
    public UserProfileDto updateMyProfile(String emailFromJwt, UpdateUserProfileRequest req) {
        User user = userRepository.findByEmail(emailFromJwt)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (req.getNickname() != null && !req.getNickname().isBlank()) {
            String newNick = req.getNickname().trim();
            if (!newNick.equals(user.getNickname()) && userRepository.existsByNickname(newNick)) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
            user.setNickname(newNick);
        }
        if (req.getUniversity() != null) {
            user.setUniversity(req.getUniversity().trim());
        }
        if (req.getMajor() != null && !req.getMajor().isBlank()) {
            user.setMajor(Major.fromKoreanName(req.getMajor().trim()));
        }
        if (req.getRecruitmentType() != null && !req.getRecruitmentType().isBlank()) {
            user.setRecruitmentType(RecruitmentType.fromKoreanName(req.getRecruitmentType().trim()));
        }

        userRepository.save(user);

        // 응답은 기존 UserProfileDto로
        return UserProfileDto.builder()
                .email(user.getEmail())   // 읽기 전용
                .nickname(user.getNickname())
                .university(user.getUniversity())
                .major(user.getMajor() != null ? user.getMajor().name() : null)
                .recruitmentType(user.getRecruitmentType() != null ? user.getRecruitmentType().name() : null)
                .isMentor(user.isMentor())
                .build();
    }
}
