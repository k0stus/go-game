package pl.kansas.go.domain.rules;

import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.domain.utils.ChainAnalyzer;

import java.util.Arrays;

public class KoRule implements Rule {

    @Override
    public void validate(Game game, Move move) {
        Board simulation = game.getBoard().copy();
        simulation.placeStone(move.getX(), move.getY(), move.getStone());

        ChainAnalyzer.removeDeadOpponentStones(
                simulation,
                move.getX(),
                move.getY(),
                move.getStone().opposite()
        );

        Stone[][] simulatedGrid = extractGrid(simulation);

        if (game.isBoardStateRepeated(simulatedGrid)) {
            throw new InvalidMoveException("Zasada Ko: Taki układ planszy już wystąpił");
        }
    }

    @Override
    public boolean check(Game game, Move move) {
        Board simulation = game.getBoard().copy();
        simulation.placeStone(move.getX(), move.getY(), move.getStone());

        ChainAnalyzer.removeDeadOpponentStones(
                simulation,
                move.getX(),
                move.getY(),
                move.getStone().opposite()
        );

        Stone[][] simulatedGrid = extractGrid(simulation);

        return !game.isBoardStateRepeated(simulatedGrid);
    }

    /**
     * Wyodrębnia siatkę kamieni ze stanu planszy.
     * @param board
     * @return
     */
    private Stone[][] extractGrid(Board board) {
        int size = board.getSize();
        Stone[][] grid = new Stone[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = board.getStone(x, y);
            }
        }
        return grid;
    }
}
