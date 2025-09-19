package example.com.chamedurefact.domain.entity;

import example.com.chamedurefact.domain.entity.mapping.ChatUser;
import example.com.chamedurefact.domain.enums.AdmissionType;
import example.com.chamedurefact.domain.enums.Major;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=20)
    private String nickname;

    @Column
    private String profileImage;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String role;

    @Column(length=20)
    private String school;

    @Column(length=20)
    private Major major;

    @Column
    private AdmissionType admissionType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChatUser> chats = new ArrayList<>();


}
