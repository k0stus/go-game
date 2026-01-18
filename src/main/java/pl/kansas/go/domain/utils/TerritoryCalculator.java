package pl.kansas.go.domain.utils;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Stone;

import java.util.*;

public class TerritoryCalculator {

    public static Map<Stone, Integer> calculateTerritory(Board board) {
        Map<Stone, Integer> scores = new HashMap<>();
        scores.put(Stone.BLACK, 0);
        scores.put(Stone.WHITE, 0);

        int size = board.getSize();
        boolean[][] visited = new boolean[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.isEmpty(x, y) && !visited[x][y]) {
                    checkTerritory(board, x, y, visited, scores);
                }
            }
        }
        return scores;
    }

    private static void checkTerritory(Board board, int startX, int startY, boolean[][] visited, Map<Stone, Integer> scores) {
        List<int[]> territoryPoints = new ArrayList<>();
        Set<Stone> touchingColors = new HashSet<>();

        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!stack.isEmpty()) {
            int[] p = stack.pop();
            territoryPoints.add(p);

            for (int[] n : board.getNeighbors(p[0], p[1])) {
                int nx = n[0];
                int ny = n[1];

                if (board.isEmpty(nx, ny)) {
                    if (!visited[nx][ny]) {
                        visited[nx][ny] = true;
                        stack.push(n);
                    }
                } else {
                    touchingColors.add(board.getStone(nx, ny));
                }
            }
        }

        if (touchingColors.size() == 1) {
            Stone owner = touchingColors.iterator().next();
            scores.put(owner, scores.get(owner) + territoryPoints.size());
        }
    }
}
