package pl.kansas.go.application;

import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serwis zarządzający sesjami gier Go.
 */
public class GameService {

    private final GameFactory gameFactory;
    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();

    public GameService(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    /**
     * Tworzy nową grę i zwraca jej unikalny identyfikator.
     *
     * @return UUID nowo utworzonej gry.
     */
    public UUID createGame() {
        Game game = gameFactory.createDefaultGame();
        GameSession session = new GameSession(game);
        sessions.put(session.getGameId(), session);
        return session.getGameId();
    }

    /**
     * Wykonuje ruch w określonej grze.
     *
     * @param gameId Identyfikator gry.
     * @param move   Ruch do wykonania.
     */
    public void makeMove(UUID gameId, Move move) {
        GameSession session = getSession(gameId);
        session.getGame().applyMove(move);
    }

    /**
     * Pobiera obiekt gry na podstawie jej identyfikatora.
     *
     * @param gameId Identyfikator gry.
     * @return Obiekt gry.
     */
    public Game getGame(UUID gameId) {
        return getSession(gameId).getGame();
    }

    /**
     * Pobiera sesję gry na podstawie jej identyfikatora.
     *
     * @param gameId Identyfikator gry.
     * @return Sesja gry.
     * @throws IllegalArgumentException jeśli gra o podanym identyfikatorze nie istnieje.
     */
    private GameSession getSession(UUID gameId) {
        GameSession session = sessions.get(gameId);
        if (session == null) {
            throw new IllegalArgumentException("Gra nie istnieje");
        }
        return session;
    }
}
