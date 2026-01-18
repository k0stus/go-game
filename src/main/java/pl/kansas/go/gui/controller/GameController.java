package pl.kansas.go.gui.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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

        presenter.setOnStateChanged(this::refresh);

        bindViewEvents();
        if (presenter.hasBoard()) {
            refresh();
        }

    }

    private void bindViewEvents() {
        //
        view.setOnCellClicked(this::handleCellClick);

        view.setOnPassAction(this::handlePass);
        view.setOnSurrenderAction(this::handleSurrender);
    }

    private void handleCellClick(int x, int y) {
        try {
            presenter.makeMove(x, y);
            refresh();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void handlePass() {
        try {
            presenter.pass();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void handleSurrender() {
        try {
            presenter.surrender();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void showGameResult(String resultMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Koniec Gry");
        alert.setHeaderText("Wynik rozgrywki");


        TextArea textArea = new TextArea(resultMessage);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();
    }

    public void refresh() {
        Platform.runLater(() -> {
            System.out.println("CONTROLLER: refresh()");

            if (presenter.hasBoard()) {
                view.updateBoard(presenter.getBoardViewModel());
            }

            if (presenter.isFinished()) {
                showGameResult(presenter.getGameResult());
            }

            view.setStatus("Ruch gracza: " + presenter.getCurrentPlayer());
        });
    }


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Niepoprawny ruch");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
