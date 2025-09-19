package example.com.chamedurefact.repository;

import example.com.chamedurefact.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
