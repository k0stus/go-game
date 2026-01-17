package pl.kansas.go.gui.presenter;

import pl.kansas.go.infrastructure.gateway.GameGateway;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

/**
 * Presenter odpowiedzialny za komunikację pomiędzy warstwą GUI
 * a warstwą aplikacyjną (GameService).
 * Nie posiada zależności od JavaFX – może być testowany jednostkowo.
 */
public class GamePresenter {

    private final GameGateway gateway;

    public GamePresenter(GameGateway gateway) {
        this.gateway = gateway;
    }

    public BoardViewModel getBoardViewModel() {
        return gateway.getBoardViewModel();
    }

    public void makeMove(int x, int y) {
        gateway.makeMove(x, y);
    }

    public Stone getCurrentPlayer() {
        return gateway.getMyStone();
    }

    /**
     * @return true, jeżeli stan planszy jest już dostępny
     */
    public boolean hasBoard() {
        return gateway.getBoardViewModel() != null;
    }

}

