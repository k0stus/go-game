package pl.kansas.go.infrastructure.network;

import pl.kansas.go.domain.model.Move;

public class MoveMessage implements Message {

    private final Move move;

    public MoveMessage(Move move) {
        this.move = move;
    }

    public Move getMove() {
        return move;
    }
}
