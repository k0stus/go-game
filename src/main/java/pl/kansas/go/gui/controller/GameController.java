package pl.kansas.go.gui.controller;

import javafx.scene.control.Alert;
import pl.kansas.go.gui.presenter.GamePresenter;
import pl.kansas.go.gui.view.GameView;

/**
 * Kontroler gry odpowiedzialny za koordynację
 * pomiędzy widokiem a presenterem.
 */
public class GameController {

    private final GamePresenter presenter;
    private final GameView view;

    public GameController(GamePresenter presenter, GameView view) {
        this.presenter = presenter;
        this.view = view;

        bindViewEvents();
        refresh();
    }

    private void bindViewEvents() {
        view.setOnCellClicked(this::handleCellClick);
    }

    private void handleCellClick(int x, int y) {
        try {
            presenter.makeMove(x, y);
            refresh();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void refresh() {
        view.updateBoard(presenter.getBoardViewModel());
        view.setStatus("Ruch gracza: " + presenter.getCurrentPlayer());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Niepoprawny ruch");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
