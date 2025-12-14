package pl.kansas.go.domain.model;

public class Player {

    private final Stone stone;

    public Player(Stone stone) {
        this.stone = stone;
    }

    public Stone getStone() {
        return stone;
    }
}
