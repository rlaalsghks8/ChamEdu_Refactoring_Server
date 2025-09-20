package example.com.chamedurefact.service;

import com.sun.tools.javac.Main;
import example.com.chamedurefact.apiPayload.code.status.ErrorStatus;
import example.com.chamedurefact.apiPayload.exception.GeneralException;
import example.com.chamedurefact.domain.entity.User;
import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import example.com.chamedurefact.domain.enums.RecruitmentType;
import example.com.chamedurefact.repository.ChatUserRepository;
import example.com.chamedurefact.repository.PostRepository;
import example.com.chamedurefact.repository.ReviewRepository;
import example.com.chamedurefact.repository.UserRepository;
import example.com.chamedurefact.web.dto.AllMentoPostResponseDto;
import example.com.chamedurefact.web.dto.MainPageResponseDto;
import example.com.chamedurefact.web.dto.MentoInfoDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class MainPageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;
    @Autowired
    private PostRepository postRepository;


    public MainPageResponseDto mainPage(String email){

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND));;

        RecruitmentType userRecruitmentType = user.getRecruitmentType();
        Major userMajor = user.getMajor();

        MainPageResponseDto dto = new MainPageResponseDto();

        dto.setPopular(populateMentoInfoDtoList());
        dto.setRecruitmentType(userRecruitmentType);
        dto.setRecommendByAdmissionType(admissionMentoInfoDtoList(userRecruitmentType));
        dto.setMajor(userMajor);
        dto.setRecommendByMajor(majorMentoInfoDtoList(userMajor));

        return dto;
    }


    public List<MentoInfoDto> populateMentoInfoDtoList() {

        List<MentoInfoDto> mentoInfoDtoList = userRepository.findTop5ByAvgReviewScoreWithPosts()
                .stream()
                .map(user -> MentoInfoDto.builder()
                        .mentoId(user.getId())
                        .name(user.getNickname())
                        .postId(postRepository.findByUser_Id(user.getId()).getId())
                        .university(user.getUniversity())
                        .major(user.getMajor())
                        .recruitmentType(user.getRecruitmentType())
                        .ratingAvg(reviewRepository.findAverageReviewScoreByUserId(user.getId()))
                        .reviewCount(reviewRepository.countByUser_Id(user.getId()))
                        .menteeCount(chatUserRepository.countByUser_id(user.getId()))
                        .build())
                .collect(Collectors.toList());

        return mentoInfoDtoList;
    }

    public List<MentoInfoDto> admissionMentoInfoDtoList(RecruitmentType recruitmentType) {
        List<MentoInfoDto> mentoInfoDtoList = userRepository.findTop5ByAdmissionTypeOrderByAvgReviewScoreAndHasPost(recruitmentType)
                .stream()
                .map(user->MentoInfoDto.builder()
                        .mentoId(user.getId())
                        .postId(postRepository.findByUser_Id(user.getId()).getId())
                        .name(user.getNickname())
                        .university(user.getUniversity())
                        .major(user.getMajor())
                        .recruitmentType(user.getRecruitmentType())
                        .ratingAvg(reviewRepository.findAverageReviewScoreByUserId(user.getId()))
                        .reviewCount(reviewRepository.countByUser_Id(user.getId()))
                        .menteeCount(chatUserRepository.countByUser_id(user.getId()))
                        .build())
                        .collect(Collectors.toList());

        return mentoInfoDtoList;
    }

    public List<MentoInfoDto> majorMentoInfoDtoList(Major major) {

        List<MentoInfoDto> mentoInfoDtoList = userRepository.findTop5ByMajorOrderByAvgReviewScoreAndHasPost(major)
                .stream()
                .map(user-> MentoInfoDto.builder()
                        .mentoId(user.getId())
                        .postId(postRepository.findByUser_Id(user.getId()).getId())
                        .name(user.getNickname())
                        .university(user.getUniversity())
                        .major(user.getMajor())
                        .recruitmentType(user.getRecruitmentType())
                        .ratingAvg(reviewRepository.findAverageReviewScoreByUserId(user.getId()))
                        .reviewCount(reviewRepository.countByUser_Id(user.getId()))
                        .menteeCount(chatUserRepository.countByUser_id(user.getId()))
                        .build())
                .collect(Collectors.toList());

        return mentoInfoDtoList;
    }


    public Page<MentoInfoDto> mentoPostFilterList(String filter, Pageable pageable){

        Page<User> users;

        if (filter.equals("ALL")) {
            users = userRepository.findAllUsersWithPosts(pageable);
        } else if (filter.equals("ReviewScore")) {
            users = userRepository.findAllUsersWithPostOrderByReviewScoreDesc(pageable);
        } else if (filter.equals("MenteeCount")) {
            users = userRepository.findTopMentorsWithPosts(pageable);
        } else {
            users = Page.empty(); // 잘못된 필터 값일 때 빈 페이지 반환
        }

        return users.map(user -> MentoInfoDto.builder()
                .mentoId(user.getId())
                .postId(postRepository.findByUser_Id(user.getId()).getId())
                .name(user.getNickname())
                .university(user.getUniversity())
                .major(user.getMajor())
                .recruitmentType(user.getRecruitmentType())
                .ratingAvg(reviewRepository.findAverageReviewScoreByUserId(user.getId()))
                .reviewCount(reviewRepository.countByUser_Id(user.getId()))
                .menteeCount(chatUserRepository.countByUser_id(user.getId()))
                .build());
    }


}
