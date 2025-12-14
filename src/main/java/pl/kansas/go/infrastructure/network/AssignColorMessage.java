package pl.kansas.go.infrastructure.network;

import pl.kansas.go.domain.model.Stone;

public class AssignColorMessage implements Message {

    private final Stone stone;

    public AssignColorMessage(Stone stone) {
        this.stone = stone;
    }

    public Stone getStone() {
        return stone;
    }
}
