package pl.kansas.go.client;

import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.infrastructure.network.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5000);

        ObjectOutputStream out =
                new ObjectOutputStream(socket.getOutputStream());
        out.flush();

        ObjectInputStream in =
                new ObjectInputStream(socket.getInputStream());

        Scanner scanner = new Scanner(System.in);
        Stone myStone = null;

        while (true) {
            Object msg = in.readObject();

            if (msg instanceof AssignColorMessage colorMsg) {
                myStone = colorMsg.getStone();
                System.out.println("Twój kolor: " + myStone);
            }

            if (msg instanceof BoardMessage boardMsg) {
                ConsoleUI.print(boardMsg.getBoard());

                if (boardMsg.getCurrentPlayer() == myStone) {
                    System.out.print("Ruch x y: ");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();

                    out.writeObject(
                            new MoveMessage(new Move(x, y, myStone))
                    );
                    out.flush();
                } else {
                    System.out.println("Czekaj na ruch przeciwnika...");
                }
            }

            if (msg instanceof ErrorMessage err) {
                System.out.println("Błąd: " + err.getMessage());
            }
        }
    }
}
