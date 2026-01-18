package pl.kansas.go.application;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.rules.PlacementRule;
import pl.kansas.go.domain.rules.Rule;

import java.util.List;

/**
 * Obiekt factory (fabryka) tworząca instancje gry Go z domyślnymi ustawieniami.
 */
public class GameFactory {

    /**
     * Tworzy domyślną grę Go z planszą 19x19 i zestawem podstawowych reguł.
     *
     * @return
     */
    public Game createDefaultGame() {
        Board board = new Board(19);

        List<Rule> rules = List.of(
                new PlacementRule()
        );

        return new Game(board, rules);
    }
}
