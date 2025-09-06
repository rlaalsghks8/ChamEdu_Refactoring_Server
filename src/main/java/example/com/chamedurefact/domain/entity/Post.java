package example.com.chamedurefact.domain.entity;

import jakarta.persistence.*;

@Entity
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String postImage;

    @Column
    private String hashTag;


}
