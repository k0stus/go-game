package pl.kansas.go.domain.model;

import java.io.Serializable;

/**
 * Reprezentuje ruch w grze Go, zawierający współrzędne oraz kolor kamienia.
 */
public class Move implements Serializable {
    private final int x;
    private final int y;
    private final Stone stone;
    private final MoveType moveType;

    public Move(int x, int y, Stone stone) {
        this.x = x;
        this.y = y;
        this.stone = stone;
        this.moveType = MoveType.PLAY;
    }

    public Move(MoveType type, Stone stone) {
        this.x = -1;
        this.y = -1;
        this.stone = stone;
        this.moveType = type;
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

    public MoveType getMoveType() {
        return moveType;
    }
}
