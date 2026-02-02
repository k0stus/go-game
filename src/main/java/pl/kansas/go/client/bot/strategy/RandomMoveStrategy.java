package pl.kansas.go.client.bot.strategy;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.MoveType;
import pl.kansas.go.domain.model.Stone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {

    private final Random random = new Random();

    @Override
    public Move findMove(Board board, Stone myStone) {
        List<Move> moves = new ArrayList<>();

        int size = board.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.isEmpty(x, y)) {
                    moves.add(new Move(x, y, myStone));
                }
            }
        }

        if (moves.isEmpty()) {
            return new Move(MoveType.PASS, myStone);
        }

        return moves.get(random.nextInt(moves.size()));
    }
}
