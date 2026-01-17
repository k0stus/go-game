package pl.kansas.go.client;

import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.infrastructure.network.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientApp {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Consumer<BoardMessage> onBoard;
    private Consumer<ErrorMessage> onError;
    private Consumer<AssignColorMessage> onAssignColor;

    public void connect(String host, int port) throws Exception {
        Socket socket = new Socket(host, port);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        Thread listener = new Thread(this::listen);
        listener.setDaemon(true);
        listener.start();
    }

    private void listen() {
        try {
            while (true) {
                Object msg = in.readObject();

                if (msg instanceof BoardMessage bm && onBoard != null)
                    onBoard.accept(bm);

                if (msg instanceof ErrorMessage em && onError != null)
                    onError.accept(em);

                if (msg instanceof AssignColorMessage ac && onAssignColor != null)
                    onAssignColor.accept(ac);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMove(Move move) {
        try {
            out.writeObject(new MoveMessage(move));
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* ===== CALLBACKI ===== */

    public void onBoard(Consumer<BoardMessage> c) {
        this.onBoard = c;
    }

    public void onError(Consumer<ErrorMessage> c) {
        this.onError = c;
    }

    public void onAssignColor(Consumer<AssignColorMessage> c) {
        this.onAssignColor = c;
    }
}
