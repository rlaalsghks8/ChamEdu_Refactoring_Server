package example.com.chamedurefact.domain.entity;

import example.com.chamedu.domain.enums.AdmissionType;
import example.com.chamedu.domain.enums.Major;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String profileImage;

    @Column
    private String email;

    @Column
    private String role;

    @Column
    private String school;

    @Column
    private Major major;

    @Column
    private AdmissionType admissionType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

}
