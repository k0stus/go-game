package pl.kansas.go.domain.utils;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Stone;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ChainAnalyzer {

    /**
     * Sprawdza, czy grupa kamieni, do której należy punkt (x, y), ma jakiekolwiek oddechy.
     */
    public static boolean hasLiberties(Board board, int startX, int startY) {
        Stone color = board.getStone(startX, startY);
        if (color == null) return true; // Puste pole ma "nieskończoność" oddechów

        Set<String> visited = new HashSet<>();
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int cx = current[0];
            int cy = current[1];
            String key = cx + "," + cy;

            if (visited.contains(key)) continue;
            visited.add(key);

            // Sprawdzamy sąsiadów
            for (int[] neighbor : board.getNeighbors(cx, cy)) {
                int nx = neighbor[0];
                int ny = neighbor[1];

                // Jeśli sąsiad jest pusty -> znaleźliśmy oddech! Grupa żyje.
                if (board.isEmpty(nx, ny)) {
                    return true;
                }

                // Jeśli sąsiad ma ten sam kolor -> jest częścią grupy, dodajemy do sprawdzenia
                if (board.getStone(nx, ny) == color) {
                    stack.push(neighbor);
                }
            }
        }

        // Przeszukaliśmy całą grupę i nie znaleźliśmy żadnego pustego pola obok
        return false;
    }

    /**
     * Usuwa martwe grupy przeciwnika (kamienie bez oddechów) i zwraca liczbę usuniętych kamieni.
     * Używane do symulacji bicia.
     */
    public static int removeDeadOpponentStones(Board board, int x, int y, Stone opponentColor) {
        int capturedCount = 0;
        Set<String> visitedGlobal = new HashSet<>();

        // Sprawdzamy wszystkich sąsiadów (potencjalne grupy przeciwnika)
        for (int[] neighbor : board.getNeighbors(x, y)) {
            int nx = neighbor[0];
            int ny = neighbor[1];

            if (board.getStone(nx, ny) == opponentColor && !visitedGlobal.contains(nx + "," + ny)) {
                // Znajdź całą grupę sąsiada
                Set<String> group = getGroup(board, nx, ny, opponentColor);

                // Sprawdź czy ta grupa ma oddechy (nie korzystamy z hasLiberties, bo musimy znać całą grupę żeby ją usunąć)
                if (!groupHasLiberties(board, group)) {
                    // Grupa martwa - usuwamy
                    for (String point : group) {
                        String[] parts = point.split(",");
                        board.removeStone(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                        capturedCount++;
                    }
                }
                visitedGlobal.addAll(group);
            }
        }
        return capturedCount;
    }

    // Pomocnicza: pobiera całą grupę kamieni
    private static Set<String> getGroup(Board board, int startX, int startY, Stone color) {
        Set<String> group = new HashSet<>();
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            String key = current[0] + "," + current[1];

            if (group.contains(key)) continue;
            group.add(key);

            for (int[] neighbor : board.getNeighbors(current[0], current[1])) {
                if (board.getStone(neighbor[0], neighbor[1]) == color) {
                    stack.push(neighbor);
                }
            }
        }
        return group;
    }

    // Pomocnicza: sprawdza oddechy dla znanej już grupy
    private static boolean groupHasLiberties(Board board, Set<String> group) {
        for (String pointKey : group) {
            String[] parts = pointKey.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            for (int[] neighbor : board.getNeighbors(x, y)) {
                if (board.isEmpty(neighbor[0], neighbor[1])) {
                    return true;
                }
            }
        }
        return false;
    }
}
