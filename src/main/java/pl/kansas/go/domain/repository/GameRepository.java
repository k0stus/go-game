package pl.kansas.go.domain.repository;

import pl.kansas.go.domain.model.Move;

import java.util.List;
import java.util.UUID;

public interface GameRepository {
    void saveMove(UUID gameId, Move move, int sequenceNumber);

    List<Move> loadMoves(UUID gameId);

    List<UUID> findAllGameIds();
}
