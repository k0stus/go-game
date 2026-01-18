package pl.kansas.go.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.kansas.go.client.ClientApp;
import pl.kansas.go.gui.controller.GameController;
import pl.kansas.go.gui.presenter.GamePresenter;
import pl.kansas.go.gui.view.GameView;
import pl.kansas.go.infrastructure.gateway.NetworkClientGateway;

/**
 * Punkt wejścia aplikacji GUI klienta Go.
 */
public class GoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        System.out.println("Starting Go GUI Client...");

        ClientApp client = new ClientApp();
        client.connect("localhost", 5123);

        System.out.println("Connected to Go server.");

        NetworkClientGateway gateway =
                new NetworkClientGateway(client);

        GamePresenter presenter =
                new GamePresenter(gateway);

        GameView view =
                new GameView();

        new GameController(presenter, view);

        stage.setScene(new Scene(view, 800, 800));
        stage.setTitle("Go – klient sieciowy");
        stage.show();

        System.out.println("Go GUI Client started.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
