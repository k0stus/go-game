package pl.kansas.go.infrastructure.server.bot;

import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Najprostsza implementacja bota.
 * Wykonuje losowy, dozwolony ruch.
 */
public class RandomBotPlayer implements BotPlayer {

    private final Stone stone;
    private final Random random = new Random();

    public RandomBotPlayer(Stone stone) {
        this.stone = stone;
    }

    @Override
    public Stone getStone() {
        return stone;
    }

    @Override
    public void onTurn(GameService gameService, UUID gameId) {
        Game game = gameService.getGame(gameId);

        if (game.getCurrentPlayer() != stone) {
            return;
        }

        List<Move> legalMoves = game.getLegalMoves(stone);

        if (legalMoves.isEmpty()) {
            return; // pass w przyszłości
        }

        Move move = legalMoves.get(random.nextInt(legalMoves.size()));
        gameService.makeMove(gameId, move);
    }
}
