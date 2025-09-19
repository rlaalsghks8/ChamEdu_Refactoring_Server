package example.com.chamedurefact.web.controller;


import example.com.chamedurefact.apiPayload.ApiResponse;
import example.com.chamedurefact.service.MentoMypageService;
import example.com.chamedurefact.web.dto.MainPageResponseDto;
import example.com.chamedurefact.web.dto.MentoMypageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MentoMyPageController {

    @Autowired
    private MentoMypageService mentoMypageService;

    @GetMapping("/chamedu/mento/mypage")
    public ApiResponse<MentoMypageResponseDto> chameduMainPage() {

        String email= null;
        MentoMypageResponseDto mentoPage = mentoMypageService.mentoMypage(email);

        return ApiResponse.onSuccess(mentoPage);
    }


}
