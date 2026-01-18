package pl.kansas.go.infrastructure.gateway;

import pl.kansas.go.client.ClientApp;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

/**
 * Brama sieciowa łącząca GUI z klientem sieciowym.
 *
 * <p>
 * Odpowiada za:
 * <ul>
 *   <li>odbiór komunikatów z serwera</li>
 *   <li>mapowanie Board → BoardViewModel</li>
 *   <li>wysyłanie ruchów gracza</li>
 * </ul>
 */
public class NetworkClientGateway implements GameGateway {

    private final ClientApp client;

    private volatile BoardViewModel boardViewModel;
    private volatile Stone myStone;
    private Runnable onStateChanged;

    /**
     * Tworzy gateway oparty o klienta sieciowego.
     *
     * @param client klient TCP
     */
    public NetworkClientGateway(ClientApp client) {
        this.client = client;

        client.onAssignColor(msg -> {
            System.out.println("Gateway: Assing Color received: " + msg.getStone());
            myStone = msg.getStone();
            notifyStateChanged();
        });

        client.onBoard(msg -> {
            System.out.println("Gateway: Board received.");
            boardViewModel = mapBoard(msg.getBoard());
            notifyStateChanged();
        });

        client.onError(msg ->
                System.err.println("Błąd z serwera: " + msg.getMessage())
        );
    }

    @Override
    public BoardViewModel getBoardViewModel() {
        return boardViewModel;
    }

    @Override
    public void makeMove(int x, int y) {
        if (myStone == null) {
            throw new IllegalStateException("Kolor gracza nie został jeszcze przypisany");
        }
        client.sendMove(new Move(x, y, myStone));
    }

    @Override
    public Stone getMyStone() {
        return myStone;
    }

    @Override
    public void setOnStateChanged(Runnable listener) {
        this.onStateChanged = listener;
    }

    private void notifyStateChanged() {
        if (onStateChanged != null) {
            onStateChanged.run();
        }
    }

    /**
     * Mapuje planszę domenową na BoardViewModel.
     */
    private BoardViewModel mapBoard(Board board) {
        int size = board.getSize();
        BoardViewModel.Cell[][] cells = new BoardViewModel.Cell[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Stone s = board.getStone(x, y);
                cells[x][y] = (s == null)
                        ? BoardViewModel.Cell.EMPTY
                        : (s == Stone.BLACK
                        ? BoardViewModel.Cell.BLACK
                        : BoardViewModel.Cell.WHITE);
            }
        }

        return new BoardViewModel(size, cells);
    }
}
