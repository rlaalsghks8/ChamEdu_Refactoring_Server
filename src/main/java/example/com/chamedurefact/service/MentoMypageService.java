package example.com.chamedurefact.service;

import example.com.chamedurefact.domain.entity.Post;
import example.com.chamedurefact.domain.entity.Review;
import example.com.chamedurefact.domain.entity.User;
import example.com.chamedurefact.repository.PostRepository;
import example.com.chamedurefact.repository.ReviewRepository;
import example.com.chamedurefact.repository.UserRepository;
import example.com.chamedurefact.web.dto.MentoMypageResponseDto;
import example.com.chamedurefact.web.dto.MentoPostResponseDto;
import example.com.chamedurefact.web.dto.PostInfoDto;
import example.com.chamedurefact.web.dto.ReviewResponseDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class MentoMypageService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;


    public MentoMypageResponseDto mentoMypage(String email){
        User  mento =  userRepository.findByEmail(email);
        Post mentoPost  = postRepository.findByUser_Id(mento.getId());

        MentoMypageResponseDto mentoMypageDto = new MentoMypageResponseDto();

        mentoMypageDto.setBackGroundImg(mentoPost.getPostImage());
        mentoMypageDto.setPostInfo(mentoPostInfo(mento.getId()));
        mentoMypageDto.setReviewList(mentoReviewList(mentoPost.getId()));


        return mentoMypageDto;
    }

    public PostInfoDto mentoPostInfo(Long userId){
        PostInfoDto postInfo = new PostInfoDto();

        Post mentoPost = postRepository.findByUser_Id(userId);

        postInfo.setPostId(mentoPost.getId());
        postInfo.setTitle(mentoPost.getTitle());
        postInfo.setContent(mentoPost.getContent());
        postInfo.setHashTag(mentoPost.getHashTag());

        return postInfo;

    }

    public List<ReviewResponseDto> mentoReviewList(Long postId){
        List<ReviewResponseDto> mentoReviews = reviewRepository.findAllByPost_Id(postId)
                .stream()
                .map(review->ReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .reviewScore(review.getReviewScore())
                        .createdAt(review.getCreatedAt())
                        .content(review.getReviewText())
                        .build())
                .collect(Collectors.toList());

        return mentoReviews;
    }
}
