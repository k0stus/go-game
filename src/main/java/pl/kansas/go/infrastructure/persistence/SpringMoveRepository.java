package pl.kansas.go.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kansas.go.infrastructure.persistence.entity.MoveEntity;

import java.util.List;
import java.util.UUID;

public interface SpringMoveRepository extends JpaRepository<MoveEntity, Long> {
    List<MoveEntity> findByGameIdOrderBySequenceNumberAsc(UUID gameId);
}
