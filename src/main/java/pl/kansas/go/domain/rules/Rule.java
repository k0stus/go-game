package pl.kansas.go.domain.rules;

import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;

public interface Rule {
    void validate(Game game, Move move);
    boolean check(Game game, Move move);
}
