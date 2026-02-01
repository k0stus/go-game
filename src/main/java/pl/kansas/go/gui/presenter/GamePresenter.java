package pl.kansas.go.gui.presenter;

import pl.kansas.go.application.GameFactory;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.infrastructure.gateway.GameGateway;
import pl.kansas.go.infrastructure.gateway.LocalReplayGateway;
import pl.kansas.go.infrastructure.gateway.NetworkClientGateway;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

import java.util.List;
import java.util.UUID;

/**
 * Presenter odpowiedzialny za komunikację pomiędzy warstwą GUI
 * a warstwą aplikacyjną (GameService).
 * Nie posiada zależności od JavaFX – może być testowany jednostkowo.
 */
public class GamePresenter {

    private final GameGateway mainGateway;
    private GameGateway activeGateway;
    private Runnable onStateChanged;

    public GamePresenter(GameGateway gateway) {
        this.mainGateway = gateway;
        this.activeGateway = gateway;
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
        return activeGateway.getBoardViewModel();
    }

    public void makeMove(int x, int y) {
        System.out.println("PRESENTER: makeMove()");
        activeGateway.makeMove(x, y);
    }

    public void pass() {
        System.out.println("PRESENTER: pass()");
        activeGateway.pass();
    }

    public void surrender() {
        System.out.println("PRESENTER: surrender()");
        activeGateway.surrender();
    }

    public boolean isFinished() {
        return activeGateway.isFinished();
    }

    public String getGameResult() {
        return activeGateway.getGameResult();
    }

    public Stone getCurrentPlayer() {
        System.out.println("PRESENTER: getCurrentPlayer");
        return activeGateway.getCurrentPlayer();
    }

    public Stone getMyStone() {
        return activeGateway.getMyStone();
    }

    public boolean isMyTurn() {
        return getMyStone() != null
                && getMyStone() == getCurrentPlayer();
    }

    /**
     * @return true, jeżeli stan planszy jest już dostępny
     */
    public boolean hasBoard() {
        return activeGateway.getBoardViewModel() != null;
    }

    // Replay Logic

    public java.util.List<UUID> fetchGameList() {
        return mainGateway.fetchGameList();
    }

    public void startReplay(UUID gameId) {
        List<Move> moves = mainGateway.fetchGameMoves(gameId);
        System.out.println("DEBUG: Fetched " + moves.size() + " moves for replay.");

        LocalReplayGateway replayGateway = new LocalReplayGateway(
                () -> new GameFactory().createDefaultGame(),
                moves);
        replayGateway.setOnStateChanged(this::onGameStateChanged);

        this.activeGateway = replayGateway;
        onGameStateChanged(); // Refresh view
    }

    public void nextMove() {
        if (activeGateway instanceof LocalReplayGateway) {
            ((LocalReplayGateway) activeGateway).next();
        }
    }

    public void prevMove() {
        if (activeGateway instanceof LocalReplayGateway) {
            ((LocalReplayGateway) activeGateway).prev();
        }
    }

    public boolean isReplayMode() {
        return activeGateway instanceof LocalReplayGateway;
    }

}
