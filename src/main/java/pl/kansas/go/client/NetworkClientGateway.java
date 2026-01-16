package pl.kansas.go.client;

import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;
import pl.kansas.go.infrastructure.network.BoardMessage;
import pl.kansas.go.infrastructure.network.MoveMessage;

/**
 * Implementacja klienta gry komunikująca się z serwerem.
 */
public class NetworkClientGateway {

    private BoardViewModel board;
    private Stone myStone;

    public NetworkClientGateway(ClientApp client) {

        client.onAssignColor(msg ->
                myStone = msg.getStone()
        );

        client.onBoard(msg ->
                board = BoardViewModel.from(msg)
        );

        client.onError(msg ->
                System.err.println(msg.getMessage())
        );
    }

    public void makeMove(int x, int y) {
        client.sendMove(new Move(x, y, myStone));
    }

    public BoardViewModel getBoard() {
        return board;
    }

    public Stone getPlayerStone() {
        return myStone;
    }
}

