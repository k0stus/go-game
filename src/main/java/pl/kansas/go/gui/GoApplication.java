package pl.kansas.go.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.kansas.go.application.GameFactory;
import pl.kansas.go.application.GameService;
import pl.kansas.go.client.ClientApp;
import pl.kansas.go.client.ClientGameGateway;
import pl.kansas.go.client.NetworkClientGateway;
import pl.kansas.go.gui.controller.GameController;
import pl.kansas.go.gui.presenter.GamePresenter;
import pl.kansas.go.gui.view.GameView;

/**
 * Główny punkt wejścia aplikacji GUI (JavaFX).
 *
 * <p>
 * Klasa odpowiada wyłącznie za:
 * <ul>
 *     <li>inicjalizację warstwy aplikacyjnej</li>
 *     <li>utworzenie prezentera</li>
 *     <li>zbudowanie widoku</li>
 *     <li>połączenie całości przez kontroler</li>
 * </ul>
 *
 * <p>
 * Nie zawiera logiki gry ani obsługi zdarzeń.
 */
public class GoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ClientApp client = new ClientApp();
        client.connect("localhost", 5000);

        NetworkClientGateway gateway = new NetworkClientGateway(client);
        GamePresenter presenter = new GamePresenter(gateway);
        GameView view = new GameView(presenter);

        new GameController(presenter, view);

        stage.setScene(new Scene(view, 800, 800));
        stage.setTitle("Go Online");
        stage.show();
    }

    /**
     * Standardowy punkt startowy JavaFX.
     *
     * @param args argumenty linii poleceń
     */
    public static void main(String[] args) {
        launch(args);
    }
}
