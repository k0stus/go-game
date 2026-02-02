package pl.kansas.go.client.bot.strategy;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.MoveType;
import pl.kansas.go.domain.model.Stone;

import java.util.List;
import java.util.Random;

public class HeuristicMoveStrategy implements MoveStrategy {

    private final Random random = new Random();

    @Override
    public Move findMove(Board board, Stone myStone) {
        int size = board.getSize();

        Move bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                if (!board.isEmpty(x, y)) continue;

                double score = evaluate(board, x, y, myStone);

                if (score > bestScore || (score == bestScore && random.nextBoolean())) {
                    bestScore = score;
                    bestMove = new Move(x, y, myStone);
                }
            }
        }

        if (bestMove == null) {
            return new Move(MoveType.PASS, myStone);
        }

        return bestMove;
    }

    private double evaluate(Board board, int x, int y, Stone myStone) {
        double score = 0.0;

        List<int[]> neighbors = board.getNeighbors(x, y);

        for (int[] n : neighbors) {
            Stone s = board.getStone(n[0], n[1]);

            if (s == null) {
                score += 0.2; // liberty
            } else if (s == myStone) {
                score += 0.5;
            } else {
                score += 1.0; // attack
            }
        }

        // kara za "izolację"
        if (neighbors.stream().allMatch(n -> board.getStone(n[0], n[1]) == null)) {
            score -= 1.5;
        }

        return score;
    }
}
