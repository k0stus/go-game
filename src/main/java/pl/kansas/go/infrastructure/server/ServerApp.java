package pl.kansas.go.infrastructure.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.model.Stone;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Główna klasa serwera aplikacji Go (Spring Boot).
 */
@SpringBootApplication
@ComponentScan(basePackages = "pl.kansas.go")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "pl.kansas.go")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "pl.kansas.go")
public class ServerApp implements CommandLineRunner {

    private static final int PORT = 5123;
    private static final int MAX_PLAYERS = 2;

    private final GameService gameService;

    public ServerApp(GameService gameService) {
        this.gameService = gameService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // separate thread for our old socket server
        startSocketServer();
    }

    private void startSocketServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serwer uruchomiony na porcie " + PORT);

        // Tworzymy domyślną grę
        var gameId = gameService.createGame();
        System.out.println("Utworzono grę: " + gameId);

        List<ClientHandler> clients = new ArrayList<>();

        while (clients.size() < MAX_PLAYERS) {
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket, gameService, gameId, clients);
            clients.add(handler);
            new Thread(handler).start();
        }

        // fixes synchronization issues
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("Sleep error: " + e.getMessage());
        }

        clients.get(0).assignStone(Stone.BLACK);
        clients.get(1).assignStone(Stone.WHITE);

        clients.get(0).broadcastBoard();
    }
}
