package example.com.chamedurefact.web.controller;

import example.com.chamedurefact.apiPayload.ApiResponse;
import example.com.chamedurefact.service.MainPageService;
import example.com.chamedurefact.web.dto.AllMentoPostResponseDto;
import example.com.chamedurefact.web.dto.MainPageResponseDto;
import example.com.chamedurefact.web.dto.MentoInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/chamedu/mainpage")
    public ApiResponse<MainPageResponseDto> chameduMainPage() {

        String email= null;
        MainPageResponseDto mainPage = mainPageService.mainPage(email);

        return ApiResponse.onSuccess(mainPage);
    }

    @GetMapping("/chamedu/postlist")
    public Page<MentoInfoDto> mentoFilterPostList(@RequestParam String filter, @RequestParam int page){

        Page<MentoInfoDto> mentoFilterList = mainPageService.mentoPostFilterList(filter, Pageable.ofSize(page));

        return mentoFilterList;
    }

}
