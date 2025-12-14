package pl.kansas.go.domain.model;

import pl.kansas.go.domain.rules.Rule;

import java.io.Serializable;
import java.util.List;

public class Game implements Serializable {

    private final Board board;
    private Stone currentPlayer;
    private final List<Rule> rules;

    public Game(Board board, List<Rule> rules) {
        this.board = board;
        this.rules = rules;
        this.currentPlayer = Stone.BLACK;
    }

    public Board getBoard() {
        return board;
    }

    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    public void applyMove(Move move) {
        if (move.getStone() != currentPlayer) {
            throw new IllegalStateException(
                    "Teraz ruch gracza: " + currentPlayer
            );
        }

        for (Rule rule : rules) {
            rule.validate(this, move);
        }

        board.placeStone(
                move.getX(),
                move.getY(),
                move.getStone()
        );
        currentPlayer = currentPlayer.opposite();
    }
}
