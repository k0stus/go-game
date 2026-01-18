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
    private Runnable onStateChanged;

    public GamePresenter(GameGateway gateway) {
        this.gateway = gateway;
        gateway.setOnStateChanged(this::onGameStateChanged);
    }

    public void setOnStateChanged(Runnable r) {
        this.onStateChanged = r;
    }

    private void onGameStateChanged() {
        System.out.println("[PRESENTER] game state changed");
        if (onStateChanged != null) {
            onStateChanged.run();
        }
    }

    public BoardViewModel getBoardViewModel() {
        System.out.println("PRESENTER: getBoardViewModel()");
        return gateway.getBoardViewModel();
    }

    public void makeMove(int x, int y) {
        System.out.println("PRESENTER: makeMove()");
        gateway.makeMove(x, y);
    }

    public Stone getCurrentPlayer() {
        System.out.println("PRESENTER: getCurrentPlayer");
        return gateway.getCurrentPlayer();
    }

    public Stone getMyStone() {
        return gateway.getMyStone();
    }

    public boolean isMyTurn() {
        return getMyStone() != null
                && getMyStone() == getCurrentPlayer();
    }

    /**
     * @return true, jeżeli stan planszy jest już dostępny
     */
    public boolean hasBoard() {
        return gateway.getBoardViewModel() != null;
    }

}

