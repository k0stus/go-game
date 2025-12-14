package pl.kansas.go.infrastructure.server;

import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.infrastructure.network.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class ClientHandler implements Runnable {

    private final GameService gameService;
    private final UUID gameId;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final List<ClientHandler> clients;

    private Stone playerStone;

    public ClientHandler(
            Socket socket,
            GameService gameService,
            UUID gameId,
            List<ClientHandler> clients
    ) throws IOException {
        this.gameService = gameService;
        this.gameId = gameId;
        this.clients = clients;

        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    public void assignStone(Stone stone) throws IOException {
        this.playerStone = stone;
        out.writeObject(new AssignColorMessage(stone));
        out.flush();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message msg = (Message) in.readObject();

                if (msg instanceof MoveMessage moveMsg) {
                    handleMove(moveMsg);
                }
            }
        } catch (Exception e) {
            System.out.println("Klient rozłączony: " + e.getMessage());
        }
    }

    private void handleMove(MoveMessage moveMsg) {
        try {
            if (playerStone != gameService
                    .getGame(gameId)
                    .getCurrentPlayer()) {
                sendError("Nie twoja kolej");
                sendBoard();   // 👈 KLUCZ
                return;
            }

            Move move = new Move(
                    moveMsg.getMove().getX(),
                    moveMsg.getMove().getY(),
                    playerStone
            );

            gameService.makeMove(gameId, move);
            broadcastBoard();

        } catch (Exception e) {
            sendError(e.getMessage());
            sendBoard();       // 👈 KLUCZ
        }
    }

    private void sendBoard() {
        try {
            var game = gameService.getGame(gameId);
            out.writeObject(
                    new BoardMessage(
                            game.getBoard().copy(),
                            game.getCurrentPlayer()
                    )
            );
            out.flush();
        } catch (Exception ignored) {
        }
    }

    private void sendError(String message) {
        try {
            out.writeObject(new ErrorMessage(message));
            out.flush();
        } catch (Exception ignored) {
        }
    }

    public void broadcastBoard() {
        try {
            var game = gameService.getGame(gameId);

            BoardMessage boardMessage = new BoardMessage(
                    game.getBoard().copy(),
                    game.getCurrentPlayer()
            );

            for (ClientHandler client : clients) {
                client.out.writeObject(boardMessage);
                client.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Błąd broadcastu: " + e.getMessage());
        }
    }
}
