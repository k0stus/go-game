package pl.kansas.go.domain.model;

import java.io.Serializable;

public class Move implements Serializable {
    private final int x;
    private final int y;
    private final Stone stone;

    public Move(int x, int y, Stone stone) {
        this.x = x;
        this.y = y;
        this.stone = stone;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Stone getStone() {
        return stone;
    }
}
