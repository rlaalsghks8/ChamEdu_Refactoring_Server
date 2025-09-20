package example.com.chamedurefact.web.controller;


import example.com.chamedurefact.apiPayload.ApiResponse;
import example.com.chamedurefact.service.MentoMypageService;
import example.com.chamedurefact.web.dto.MainPageResponseDto;
import example.com.chamedurefact.web.dto.MentoMypageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MentoMyPageController {

    @Autowired
    private MentoMypageService mentoMypageService;

    @Operation(summary = "멘토 마이페이지", description = "멘토 마이페이지 api")
    @GetMapping("/chamedu/mento/mypage")
    public ApiResponse<MentoMypageResponseDto> chameduMainPage(Authentication authentication) {

        String email= (String)authentication.getPrincipal();
        MentoMypageResponseDto mentoPage = mentoMypageService.mentoMypage(email);

        return ApiResponse.onSuccess(mentoPage);
    }


}
