package example.com.chamedurefact.domain.entity;

import jakarta.persistence.*;

@Entity
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int reviewScore;

    @Column(nullable = true)
    private String reviewText;
}
