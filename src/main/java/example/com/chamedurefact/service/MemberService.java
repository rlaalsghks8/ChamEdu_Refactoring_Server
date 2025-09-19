package example.com.chamedurefact.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.com.chamedurefact.repository.UserRepository;
import example.com.chamedurefact.web.dto.KakaoTokenResponseDto;
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

        //응답 Body를 파싱, 최상위 필드의 access_token을 꺼냄
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

            // email: kakao_account.email (동의 필요)
            String email = null;
            JsonNode kakaoAccount = root.path("kakao_account");
            if (kakaoAccount.isObject()) {
                JsonNode emailNode = kakaoAccount.path("email");
                if (emailNode.isTextual()) email = emailNode.asText();
            }

            // nickname: kakao_account.profile.nickname 우선, 없으면 properties.nickname
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

            // 확인용(마스킹)
            log.info("Kakao user email={}, nickname={}",
                    email != null ? email : "(null)",
                    nickname != null ? nickname : "(null)");

            return userInfo;

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 파싱 실패", e);
        }
    }

    //사용자 정보 확인, 없으면 회원가입 후 로그인, 있으면 로그인 (JWT 토큰 발급)

    // 로그인 시 추가정보 설정 여부 보내줘야 하나?
}
