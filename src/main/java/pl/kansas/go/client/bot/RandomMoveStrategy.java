package pl.kansas.go.client.bot;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.MoveType;
import pl.kansas.go.domain.model.Stone;

import java.util.List;
import java.util.Random;

/**
 * Strategia losowego legalnego ruchu.
 */
public class RandomMoveStrategy {
    private final Random random = new Random();

    public Move findMove(Board board, Stone stone) {
        List<Move> legalMoves = board.getLegalMoves(stone);
        return legalMoves.get(random.nextInt(legalMoves.size()));
    }
}
