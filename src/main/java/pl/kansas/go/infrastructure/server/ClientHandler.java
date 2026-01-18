package pl.kansas.go.infrastructure.server;

import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.exception.InvalidMoveException;
import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.MoveType;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.infrastructure.network.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

/**
 * Klasa do obsługi (pojedyńczego) klienta na serwerze Go.
 */
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

    /**
     * Przypisuje kolor kamienia graczowi i wysyła odpowiednią wiadomość.
     *
     * @param stone Kolor kamienia do przypisania graczowi.
     * @throws IOException W przypadku błędu wejścia/wyjścia podczas wysyłania wiadomości.
     */
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

    /**
     * Obsługuje ruch gracza.
     *
     * @param moveMsg Wiadomość zawierająca informacje o ruchu gracza.
     */
    private void handleMove(MoveMessage moveMsg) {
        try {
            Game game = gameService.getGame(gameId);

            if (playerStone != game.getCurrentPlayer()) {
                sendError("Nie twoja kolej");
                sendBoard();
                return;
            }

            Move incomingMove = moveMsg.getMove();
            Move moveToServer;

            if (incomingMove.getMoveType() == MoveType.PLAY) {
                moveToServer = new Move(
                        incomingMove.getX(),
                        incomingMove.getY(),
                        playerStone
                );
            } else {
                moveToServer = new Move(incomingMove.getMoveType(), playerStone);
            }

            gameService.makeMove(gameId, moveToServer);
            broadcastBoard();

        } catch (Exception e) {
            sendError(e.getMessage());
            sendBoard();

        }
    }

    /**
     * Wysyła aktualny stan planszy do klienta.
     */
    private void sendBoard() {
        try {
            var game = gameService.getGame(gameId);
            out.writeObject(
                    new BoardMessage(
                            game.getBoard().copy(),
                            game.getCurrentPlayer(),
                            game.isFinished(),
                            game.getGameResult()
                    )
            );
            out.flush();
        } catch (Exception ignored) {
        }
    }

    /**
     * Wysyła wiadomość o błędzie do klienta.
     *
     * @param message Treść wiadomości o błędzie.
     */
    private void sendError(String message) {
        try {
            out.writeObject(new ErrorMessage(message));
            out.flush();
        } catch (Exception ignored) {
        }
    }

    /**
     * Rozsyła aktualny stan planszy do wszystkich podłączonych klientów.
     */
    public void broadcastBoard() {
        try {
            var game = gameService.getGame(gameId);

            BoardMessage boardMessage = new BoardMessage(
                    game.getBoard().copy(),
                    game.getCurrentPlayer(),
                    game.isFinished(),
                    game.getGameResult()
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
