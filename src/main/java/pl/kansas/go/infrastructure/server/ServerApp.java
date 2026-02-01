package pl.kansas.go.infrastructure.server;

import pl.kansas.go.application.GameFactory;
import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.infrastructure.server.bot.RandomBotPlayer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Główna klasa serwera aplikacji Go.
 * Uruchamia grę w trybie: 1 gracz + 1 bot.
 */
public class ServerApp {

    private static final int PORT = 5123;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serwer uruchomiony na porcie " + PORT);

        GameService gameService = new GameService(new GameFactory());
        var gameId = gameService.createGame();

        List<ClientHandler> clients = new ArrayList<>();

        System.out.println("Oczekiwanie na klienta...");
        Socket socket = serverSocket.accept();
        System.out.println("Klient połączony");

        // Tworzymy bota
        RandomBotPlayer bot = new RandomBotPlayer(Stone.WHITE);

        ClientHandler handler =
                new ClientHandler(socket, gameService, gameId, clients, bot);

        clients.add(handler);
        new Thread(handler).start();

        // Przypisanie koloru graczowi
        handler.assignStone(Stone.BLACK);

        // Start gry – wysyłamy początkową planszę
        handler.broadcastBoard();
    }
}
