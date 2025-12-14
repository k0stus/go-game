package pl.kansas.go.domain.rules;

import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;

public class PlacementRule implements Rule {

    @Override
    public void validate(Game game, Move move) {
        if (!game.getBoard().isEmpty(move.getX(), move.getY())) {
            throw new InvalidMoveException("Pole jest już zajęte");
        }
    }
}
