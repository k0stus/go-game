package pl.kansas.go.infrastructure.server;

import pl.kansas.go.application.GameFactory;
import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.model.Stone;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Główna klasa serwera aplikacji Go.
 */
public class ServerApp {

    private static final int PORT = 5123;
    private static final int MAX_PLAYERS = 2;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Serwer uruchomiony na porcie " + PORT);

        GameService gameService = new GameService(new GameFactory());
        var gameId = gameService.createGame();

        List<ClientHandler> clients = new ArrayList<>();

        while (clients.size() < MAX_PLAYERS) {
            Socket socket = serverSocket.accept();
            ClientHandler handler =
                    new ClientHandler(socket, gameService, gameId, clients);
            clients.add(handler);
            new Thread(handler).start();
        }

        clients.get(0).assignStone(Stone.BLACK);
        clients.get(1).assignStone(Stone.WHITE);

        clients.get(0).broadcastBoard();
    }
}
