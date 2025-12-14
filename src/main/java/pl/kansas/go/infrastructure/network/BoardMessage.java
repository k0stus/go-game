package pl.kansas.go.infrastructure.network;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Stone;

import java.io.Serializable;

public class BoardMessage implements Message, Serializable {

    private final Board board;
    private final Stone currentPlayer;

    public BoardMessage(Board board, Stone currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }
}
