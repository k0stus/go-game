package pl.kansas.go.gui.presenter;

import pl.kansas.go.application.GameService;
import pl.kansas.go.client.ClientGameGateway;
import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

import java.util.UUID;

/**
 * Presenter odpowiedzialny za komunikację pomiędzy warstwą GUI
 * a warstwą aplikacyjną (GameService).
 * Nie posiada zależności od JavaFX – może być testowany jednostkowo.
 */
public class GamePresenter {

    private final ClientGameGateway client;

    public GamePresenter(ClientGameGateway client) {
        this.client = client;
    }

    public void makeMove(int x, int y) {
        client.sendMove(x, y);
    }

    public BoardViewModel getBoardViewModel() {
        return client.getBoard();
    }

    public Stone getCurrentPlayer() {
        return client.getPlayerColor();
    }
}
