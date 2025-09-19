package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.mapping.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    Integer countByUser_id(Long id);
}
