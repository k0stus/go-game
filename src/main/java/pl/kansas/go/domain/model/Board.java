package pl.kansas.go.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje planszę do gry Go o określonym rozmiarze.
 */
public class Board implements Serializable {

    private final int size;
    private final Stone[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new Stone[size][size];
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty(int x, int y) {
        checkBounds(x, y);
        return grid[x][y] == null;
    }

    public Stone getStone(int x, int y) {
        checkBounds(x, y);
        return grid[x][y];
    }

    public void placeStone(int x, int y, Stone stone) {
        checkBounds(x, y);
        grid[x][y] = stone;
    }

    public void removeStone(int x, int y) {
        checkBounds(x, y);
        grid[x][y] = null;
    }

    /**
     * Zwraca listę współrzędnych sąsiednich pól dla podanego pola (x, y).
     * @param x wspolrzedna x
     * @param y wspolrzedna y
     * @return lista
     */
    public List<int[]> getNeighbors(int x, int y) {
        List<int[]> neighbors = new ArrayList<>();

        if (x > 0) neighbors.add(new int[]{x - 1, y});
        if (x < size - 1) neighbors.add(new int[]{x + 1, y});
        if (y > 0) neighbors.add(new int[]{x, y - 1});
        if (y < size - 1) neighbors.add(new int[]{x, y + 1});

        return neighbors;
    }

    /**
     * Sprawdza, czy podane współrzędne (x, y) mieszczą się w granicach planszy.
     *
     * @param x
     * @param y
     */
    private void checkBounds(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException("Poza planszą");
        }
    }

    /**
     * Tworzy i zwraca kopię planszy.
     *
     * @return kopia planszy
     */
    public Board copy() {
        Board copy = new Board(size);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                copy.grid[x][y] = this.grid[x][y];
            }
        }
        return copy;
    }

    /**
     * Zwraca listę potencjalnych ruchów (na pustych polach).
     * Pełna walidacja odbywa się po stronie serwera.
     */
    public List<Move> getLegalMoves(Stone stone) {
        List<Move> moves = new ArrayList<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y] == null) {
                    moves.add(new Move(x, y, stone));
                }
            }
        }

        // Jeżeli nie ma żadnych ruchów – PASS
        if (moves.isEmpty()) {
            moves.add(new Move(MoveType.PASS, stone));
        }

        return moves;
    }

}
