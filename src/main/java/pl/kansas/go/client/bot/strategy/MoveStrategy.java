package pl.kansas.go.client.bot.strategy;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;

public interface MoveStrategy {

    Move findMove(Board board, Stone stone);
}
