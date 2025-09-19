// PostActivityController.java
package example.com.chamedurefact.web.controller;

import example.com.chamedurefact.service.PostService;
import example.com.chamedurefact.web.dto.DefaultResponseDto;
import example.com.chamedurefact.web.dto.RecentPostItemDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/{postId}/view")
    @Operation(summary = "게시글 상세 진입 기록", description = "멘티가 게시글 상세 페이지 들어올 때 호출")
    public ResponseEntity<DefaultResponseDto<Void>> recordView(@PathVariable Long postId, Authentication authentication) {
        String email = (String) authentication.getPrincipal(); // JwtAuthenticationFilter 에서 세팅
        postService.recordView(email, postId);

        return ResponseEntity.ok(
                DefaultResponseDto.<Void>builder()
                        .isSuccess(true)
                        .code("2100")
                        .message("조회 기록 저장")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/recent")
    @Operation(summary = "최근 본 게시글 목록", description = "최근 본 게시글을 최신순으로 반환")
    public ResponseEntity<DefaultResponseDto<List<RecentPostItemDto>>> recent(@RequestParam(defaultValue = "10") int limit,
                                                                              Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        List<RecentPostItemDto> list = postService.getRecentViews(email, limit);

        return ResponseEntity.ok(
                DefaultResponseDto.<List<RecentPostItemDto>>builder()
                        .isSuccess(true)
                        .code("2101")
                        .message("최근 본 게시글 조회 성공")
                        .data(list)
                        .build()
        );
    }
}
