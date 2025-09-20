package example.com.chamedurefact.web.controller;

import example.com.chamedurefact.apiPayload.ApiResponse;
import example.com.chamedurefact.service.MainPageService;
import example.com.chamedurefact.web.dto.AllMentoPostResponseDto;
import example.com.chamedurefact.web.dto.MainPageResponseDto;
import example.com.chamedurefact.web.dto.MentoInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainPageController {

    @Autowired
    private MainPageService mainPageService;

    @Operation(summary = "메인페이지", description = "메인페이지 api")
    @GetMapping("/chamedu/mainpage")
    public ApiResponse<MainPageResponseDto> chameduMainPage(Authentication authentication) {

        String email= (String)authentication.getPrincipal();
        MainPageResponseDto mainPage = mainPageService.mainPage(email);

        return ApiResponse.onSuccess(mainPage);
    }

    @Operation(summary = "메인페이지(필터링)", description = "메인페이지(필터링) api")
    @GetMapping("/chamedu/postlist")
    public Page<MentoInfoDto> mentoFilterPostList(@RequestParam String filter, @RequestParam int page){

        Page<MentoInfoDto> mentoFilterList = mainPageService.mentoPostFilterList(filter, Pageable.ofSize(page));

        return mentoFilterList;
    }

}
