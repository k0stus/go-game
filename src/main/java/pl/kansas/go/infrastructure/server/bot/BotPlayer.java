package pl.kansas.go.infrastructure.server.bot;

import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.model.Stone;

import java.util.UUID;

public interface BotPlayer {
    Stone getStone();
    void onTurn(GameService gameService, UUID gameId);
}
