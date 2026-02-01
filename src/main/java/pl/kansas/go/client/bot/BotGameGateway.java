package pl.kansas.go.client.bot;

import pl.kansas.go.client.ClientApp;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;

/**
 * Gateway sieciowy dla bota (bez GUI).
 */
public class BotGameGateway {

    private final ClientApp client;

    private Board board;
    private Stone myStone;
    private Stone currentPlayer;
    private boolean finished;

    public BotGameGateway(ClientApp client) {
        this.client = client;

        client.onAssignColor(msg -> {
            myStone = msg.getStone();
            System.out.println("[BOT] Assigned stone: " + myStone);
        });

        client.onBoard(msg -> {
            board = msg.getBoard();
            currentPlayer = msg.getCurrentPlayer();
            finished = msg.isFinished();
        });
    }

    public boolean isMyTurn() {
        return myStone != null && myStone == currentPlayer;
    }

    public boolean isFinished() {
        return finished;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getMyStone() {
        return myStone;
    }

    public void sendMove(Move move) {
        client.sendMove(move);
    }
}
