package pl.kansas.go.gui.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import pl.kansas.go.domain.model.Stone;

/**
 * Pasek statusu wyświetlający informacje o stanie gry,
 * takie jak aktualny gracz lub komunikaty dla użytkownika.
 */
public class StatusBar extends HBox {

    private final Label statusLabel = new Label();
    private final Button passButton = new Button("Pass");
    private final Button surrenderButton = new Button("Poddaj się");

    /**
     * Tworzy pasek statusu.
     */
    public StatusBar() {
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setStyle("-fx-padding: 8; -fx-background-color: #eeeeee; -fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");


        // space to imitate justify-content: space-between
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(statusLabel, spacer, passButton, surrenderButton);
    }

    /**
     * Aktualizuje informację o aktualnym graczu.
     *
     * @param player aktualny gracz
     */
    public void showCurrentPlayer(Stone player) {
        statusLabel.setText("Ruch gracza: " + player);
    }

    /**
     * Wyświetla komunikat informacyjny.
     *
     * @param message treść komunikatu
     */
    public void showMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Ustawia akcję do wykonania po kliknięciu "Pass".
     *
     * @param action akcja
     */
    public void setOnPassAction(Runnable action) {
        passButton.setOnAction(event -> action.run());
    }

    /**
     * Ustawia akcję do wykonania po kliknięciu "Poddaj się".
     *
     * @param action akcja
     */
    public void setOnSurrenderAction(Runnable action) {
        surrenderButton.setOnAction(event -> action.run());
    }

    public void setButtonsDisabled(boolean disabled) {
        passButton.setDisable(disabled);
        surrenderButton.setDisable(disabled);
    }

}
