// RecentPostService.java
package example.com.chamedurefact.service;

import example.com.chamedurefact.domain.entity.Post;
import example.com.chamedurefact.domain.entity.PostView;
import example.com.chamedurefact.domain.entity.User;
import example.com.chamedurefact.repository.PostRepository;
import example.com.chamedurefact.repository.PostViewRepository;
import example.com.chamedurefact.repository.UserRepository;
import example.com.chamedurefact.web.dto.RecentPostItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostViewRepository postViewRepository;

    //상세 페이지 진입 시 호출, 기록
    @Transactional
    public void recordView(String viewerEmail, Long postId) {
        User viewer = userRepository.findByEmail(viewerEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        PostView pv = postViewRepository.findByViewer_IdAndPost_Id(viewer.getId(), postId)
                .orElse(PostView.builder()
                        .viewer(viewer)
                        .post(post)
                        .lastViewedAt(LocalDateTime.now())
                        .viewCount(0L)
                        .build());

        pv.touch(); // lastViewedAt now, viewCount+1
        postViewRepository.save(pv);
    }

    //최근 본 글 조회
    @Transactional(readOnly = true)
    public List<RecentPostItemDto> getRecentViews(String viewerEmail, int limit) {
        User viewer = userRepository.findByEmail(viewerEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<PostView> rows = postViewRepository.findRecentByViewerId(viewer.getId(), PageRequest.of(0, Math.max(1, Math.min(limit, 50))));

        return rows.stream().map(pv -> {
            Post p = pv.getPost();
            return RecentPostItemDto.builder()
                    .postId(p.getId())
                    .title(p.getTitle())
                    .postImage(p.getPostImage())
                    .hashTag(p.getHashTag())
                    .authorNickname(p.getUser() != null ? p.getUser().getNickname() : null)
                    .lastViewedAt(pv.getLastViewedAt())
                    .build();
        }).collect(toList());
    }
}
