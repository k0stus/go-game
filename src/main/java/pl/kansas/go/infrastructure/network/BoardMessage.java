package pl.kansas.go.infrastructure.network;

import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Stone;

import java.io.Serializable;

public class BoardMessage implements Message, Serializable {

    private final Board board;
    private final Stone currentPlayer;
    private final boolean isFinished;
    private final String gameResult;


    public BoardMessage(Board board, Stone currentPlayer) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.isFinished = false;
        this.gameResult = null;
    }

    public BoardMessage(Board board, Stone currentPlayer, String gameResult) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.isFinished = true;
        this.gameResult = gameResult;
    }

    public BoardMessage(Board board, Stone currentPlayer, boolean isFinished, String gameResult) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.isFinished = isFinished;
        this.gameResult = gameResult;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String getGameResult() {
        return gameResult;
    }
}
