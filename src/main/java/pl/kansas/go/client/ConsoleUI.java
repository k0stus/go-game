package pl.kansas.go.client;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Stone;

public class ConsoleUI {

    public static void print(Board board) {
        int size = board.getSize();

        System.out.println();
        System.out.print("   ");
        for (int x = 0; x < size; x++) {
            System.out.printf("%2d ", x);
        }
        System.out.println();

        for (int y = 0; y < size; y++) {
            System.out.printf("%2d ", y);
            for (int x = 0; x < size; x++) {
                Stone stone = board.getStone(x, y);
                if (stone == null) {
                    System.out.print(" . ");
                } else if (stone == Stone.BLACK) {
                    System.out.print(" X ");
                } else {
                    System.out.print(" O ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
