package example.com.chamedurefact.service;

import example.com.chamedurefact.apiPayload.code.status.ErrorStatus;
import example.com.chamedurefact.apiPayload.exception.GeneralException;
import example.com.chamedurefact.apiPayload.code.status.ErrorStatus;
import example.com.chamedurefact.apiPayload.exception.GeneralException;
import example.com.chamedurefact.domain.entity.Post;
import example.com.chamedurefact.domain.entity.PostView;
import example.com.chamedurefact.domain.entity.User;
import example.com.chamedurefact.repository.PostRepository;
import example.com.chamedurefact.repository.PostViewRepository;
import example.com.chamedurefact.repository.ReviewRepository;
import example.com.chamedurefact.repository.UserRepository;
import example.com.chamedurefact.web.dto.MentoInfoDto;
import example.com.chamedurefact.web.dto.MentoPostResponseDto;
import example.com.chamedurefact.web.dto.PostRequestDto;
import example.com.chamedurefact.web.dto.ReviewResponseDto;
import jakarta.transaction.Transactional;
import lombok.Data;
import example.com.chamedurefact.web.dto.RecentPostItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Data
@RequiredArgsConstructor
public class PostService {


    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public void updatePost(Long postId, PostRequestDto postDto, String email) {
        // 1. postId로 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND));

        // 2. 작성자 검증 (본인 글만 수정 가능하도록)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            new GeneralException(ErrorStatus._FORBIDDEN);
        }

        // 3. 게시글 수정
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setHashTag(postDto.getHashTag());
        post.setAdmissionType(postDto.getAdmissionType());
        post.setMajor(postDto.getMajor());

        // JPA @Transactional 환경에서는 save 호출 안 해도 dirty checking으로 업데이트 반영됨
        // postRepository.save(post); -> 필요 없음
    }

    @Transactional
    public void postWrite(PostRequestDto postDto, String email){

        Post post = new Post();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setHashTag(postDto.getHashTag());
        post.setAdmissionType(postDto.getAdmissionType());
        post.setMajor(postDto.getMajor());
        post.setUser(user);

        postRepository.save(post);
    }

    public MentoPostResponseDto mentoPost(Long postId){

        MentoPostResponseDto mentoPostDto = new MentoPostResponseDto();

        mentoPostDto.setMentoInfoDto(mentoInfo(postId));
        mentoPostDto.setReviewList(reviewList(postId));

        return mentoPostDto;

    }

    public MentoInfoDto mentoInfo(Long postId){

        User mento = userRepository.findByPostId(postId);

        MentoInfoDto mentoInfo = MentoInfoDto.builder()
                .mentoId(mento.getId())
                .email(mento.getEmail())
                .name(mento.getNickname())
                .university(mento.getUniversity())
                .major(mento.getMajor())
                .build();

        return mentoInfo;
    }

    public List<ReviewResponseDto> reviewList(Long postId){

        List<ReviewResponseDto> userReviews = reviewRepository.findAllByPost_Id(postId)
                .stream()
                .map(review->ReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .reviewScore(review.getReviewScore())
                        .createdAt(review.getCreatedAt())
                        .content(review.getReviewText())
                        .build())
                .collect(Collectors.toList());


        return userReviews;
    }

    @org.springframework.transaction.annotation.Transactional
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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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


    private PostViewRepository postViewRepository;


}
