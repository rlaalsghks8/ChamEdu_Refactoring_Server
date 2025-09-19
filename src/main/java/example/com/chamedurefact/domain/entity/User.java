package example.com.chamedurefact.domain.entity;

import example.com.chamedurefact.domain.enums.Major;
import example.com.chamedurefact.domain.enums.RecruitmentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=20)
    private String nickname;

    @Column
    private String realName;

    @Column
    private String profileImage;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String role;

    @Column(length=20)
    private String university; // school -> university로 변경

    @Enumerated(EnumType.STRING)
    private Major major;

    @Enumerated(EnumType.STRING)
    private RecruitmentType recruitmentType;

    @Column
    private boolean isMentor;

    @Column
    private Boolean isProfileSetup; // 첫 로그인 시 프로필 설정 여부

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<ChatUser> chats = new ArrayList<>();
}