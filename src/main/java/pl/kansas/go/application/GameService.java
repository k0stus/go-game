package pl.kansas.go.application;

import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameService {

    private final GameFactory gameFactory;
    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();

    public GameService(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    public UUID createGame() {
        Game game = gameFactory.createDefaultGame();
        GameSession session = new GameSession(game);
        sessions.put(session.getGameId(), session);
        return session.getGameId();
    }

    public void makeMove(UUID gameId, Move move) {
        GameSession session = getSession(gameId);
        session.getGame().applyMove(move);
    }

    public Game getGame(UUID gameId) {
        return getSession(gameId).getGame();
    }

    private GameSession getSession(UUID gameId) {
        GameSession session = sessions.get(gameId);
        if (session == null) {
            throw new IllegalArgumentException("Gra nie istnieje");
        }
        return session;
    }
}
