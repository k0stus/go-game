package pl.kansas.go.application;

import pl.kansas.go.domain.model.Game;

import java.util.UUID;

/**
 * Reprezentuje sesję gry Go, zawierającą unikalny identyfikator oraz instancję gry.
 */
public class GameSession {

    private final UUID gameId;
    private final Game game;

    public GameSession(Game game) {
        this.gameId = UUID.randomUUID();
        this.game = game;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Game getGame() {
        return game;
    }
}
