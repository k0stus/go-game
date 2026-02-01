package pl.kansas.go.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kansas.go.infrastructure.persistence.entity.GameEntity;

import java.util.UUID;

public interface SpringGameRepository extends JpaRepository<GameEntity, UUID> {
}
