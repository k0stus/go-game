package pl.kansas.go.application;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.rules.LibertyRule;
import pl.kansas.go.domain.rules.PlacementRule;
import pl.kansas.go.domain.rules.Rule;

import java.util.List;

public class GameFactory {

    public Game createDefaultGame() {
        Board board = new Board(19);

        List<Rule> rules = List.of(
                new PlacementRule(),
                new LibertyRule()
        );

        return new Game(board, rules);
    }
}
