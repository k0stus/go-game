package pl.kansas.go.domain.rules;

import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.domain.utils.ChainAnalyzer;

public class SuicideRule implements Rule {

    @Override
    public void validate(Game game, Move move) {

        // use copy of the board to simulate the move
        Board simulation = game.getBoard().copy();
        simulation.placeStone(move.getX(), move.getY(), move.getStone());


        Stone opponent = move.getStone().opposite();
        int captured = ChainAnalyzer.removeDeadOpponentStones(simulation, move.getX(), move.getY(), opponent);

        if (captured > 0) {
            return;
        }

        if (!ChainAnalyzer.hasLiberties(simulation, move.getX(), move.getY())) {
            throw new InvalidMoveException("Ruch samobójczy jest niedozwolony");
        }
    }

    @Override
    public boolean check(Game game, Move move) {

        // use copy of the board to simulate the move
        Board simulation = game.getBoard().copy();
        simulation.placeStone(move.getX(), move.getY(), move.getStone());


        Stone opponent = move.getStone().opposite();
        int captured = ChainAnalyzer.removeDeadOpponentStones(simulation, move.getX(), move.getY(), opponent);

        if (captured > 0) {
            return true;
        }

        return ChainAnalyzer.hasLiberties(simulation, move.getX(), move.getY());
    }
}
