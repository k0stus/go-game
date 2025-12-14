package pl.kansas.go.domain.rules;

import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;

public class LibertyRule implements Rule {

    @Override
    public void validate(Game game, Move move) {
        Board board = game.getBoard();

        boolean hasLiberty = board.getNeighbors(move.getX(), move.getY()).stream()
                .anyMatch(p -> board.isEmpty(p[0], p[1]));

        if (!hasLiberty) {
            throw new InvalidMoveException("Ruch bez oddechu");
        }
    }
}
