package example.com.chamedurefact.web.controller;


import example.com.chamedurefact.apiPayload.ApiResponse;
import example.com.chamedurefact.service.PostService;
import example.com.chamedurefact.web.dto.MentoPostResponseDto;
import example.com.chamedurefact.web.dto.PostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/chamedu/post")
    public ApiResponse<MentoPostResponseDto> mentoPostInfo(@RequestParam Long postId){

        MentoPostResponseDto mentoPostInfo = postService.mentoPost(postId);
        if(mentoPostInfo == null){
            ApiResponse.onFailure("Not Found Exception 404","Not Found",null);
        }

        return ApiResponse.onSuccess(mentoPostInfo);
    }

    @PostMapping("/chamedu/mento/posts")
    public ApiResponse<String> postWrite(@RequestBody PostRequestDto dto){

        String email = null;
        postService.postWrite(dto,email);

        return ApiResponse.onSuccess("게시글 작성 성공!");
    }

    @PutMapping("/chamedu/mento/posts/{postId}")
    public ApiResponse<String> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto dto
    ) {
        String email = null; // TODO: 로그인 인증에서 email 추출해서 주입
        postService.updatePost(postId, dto, email);
        return ApiResponse.onSuccess("게시글 수정 성공!");
    }
}
