package pl.kansas.go.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.MoveType;
import pl.kansas.go.domain.repository.GameRepository;
import pl.kansas.go.infrastructure.persistence.entity.GameEntity;
import pl.kansas.go.infrastructure.persistence.entity.MoveEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JPA implementation of the GameRepository interface.
 */
@Repository
public class JpaGameRepositoryAdapter implements GameRepository {

    private final SpringGameRepository gameRepository;
    private final SpringMoveRepository moveRepository;

    public JpaGameRepositoryAdapter(SpringGameRepository gameRepository, SpringMoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
    }

    @Override
    public void saveMove(UUID gameId, Move move, int sequenceNumber) {
        if (!gameRepository.existsById(gameId)) {
            gameRepository.save(new GameEntity(gameId, LocalDateTime.now()));
        }

        MoveEntity entity = new MoveEntity(
                gameId,
                sequenceNumber,
                move.getX(),
                move.getY(),
                move.getStone(),
                move.getMoveType());
        moveRepository.save(entity);
    }

    @Override
    public List<Move> loadMoves(UUID gameId) {
        return moveRepository.findByGameIdOrderBySequenceNumberAsc(gameId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UUID> findAllGameIds() {
        return gameRepository.findAll().stream()
                .map(GameEntity::getId)
                .collect(Collectors.toList());
    }

    private Move toDomain(MoveEntity entity) {
        if (entity.getType() == MoveType.PASS || entity.getType() == MoveType.SURRENDER) {
            return new Move(entity.getType(), entity.getStone());
        }

        return new Move(entity.getX(), entity.getY(), entity.getStone());
    }
}
