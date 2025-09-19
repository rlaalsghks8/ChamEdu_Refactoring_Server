package example.com.chamedurefact.service;


import example.com.chamedurefact.repository.PostRepository;
import example.com.chamedurefact.repository.ReviewRepository;
import example.com.chamedurefact.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

}
