package pl.kansas.go.infrastructure.server;

import pl.kansas.go.application.GameService;
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
    private Stone playerStone;
    private final List<ClientHandler> clients;

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

                    if (playerStone !=
                            gameService.getGame(gameId).getCurrentPlayer()) {
                        out.writeObject(
                                new ErrorMessage("Nie twoja kolej")
                        );
                        out.flush();
                        continue;
                    }

                    Move serverMove = new Move(
                            moveMsg.getMove().getX(),
                            moveMsg.getMove().getY(),
                            playerStone
                    );

                    gameService.makeMove(gameId, serverMove);
                    broadcastBoard();
                }
            }
        } catch (Exception e) {
            System.out.println("Klient rozłączony");
        }
    }

    public void broadcastBoard() throws IOException {
        var game = gameService.getGame(gameId);

        BoardMessage boardMessage = new BoardMessage(
                game.getBoard().copy(),   // SNAPSHOT
                game.getCurrentPlayer()
        );

        for (ClientHandler client : clients) {
            client.out.writeObject(boardMessage);
            client.out.flush();
        }
    }
}
