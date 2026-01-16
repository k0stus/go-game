package pl.kansas.go.gui.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import pl.kansas.go.domain.model.Stone;

/**
 * Pasek statusu wyświetlający informacje o stanie gry,
 * takie jak aktualny gracz lub komunikaty dla użytkownika.
 */
public class StatusBar extends HBox {

    private final Label statusLabel = new Label();

    /**
     * Tworzy pasek statusu.
     */
    public StatusBar() {
        getChildren().add(statusLabel);
        setStyle("-fx-padding: 8; -fx-background-color: #eeeeee;");
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
}
