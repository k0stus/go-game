package pl.kansas.go.domain.model;

import pl.kansas.go.domain.rules.Rule;
import pl.kansas.go.domain.utils.ChainAnalyzer;
import pl.kansas.go.domain.utils.TerritoryCalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Reprezentuje stan gry Go, w tym planszę, aktualnego gracza oraz zestaw reguł gry.
 */
public class Game implements Serializable {

    private final Board board;
    private Stone currentPlayer;
    private final List<Rule> rules;
    private final List<Stone[][]> history = new ArrayList<>();
    private boolean finished = false;
    private MoveType lastMoveType = null;
    private int prisonersBlack = 0;
    private int prisonersWhite = 0;
    private String gameResultMessage = null;

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
    public int getPrisonersBlack() { return prisonersBlack; }
    public int getPrisonersWhite() { return prisonersWhite; }

    public void applyMove(Move move) {
        // initial cheks
        if (finished) {
            throw new IllegalStateException("Gra jest już zakończona.");
        }

        if (move.getStone() != currentPlayer) {
            throw new IllegalStateException(
                    "Teraz ruch gracza: " + currentPlayer
            );
        }

        // pass and surrender handling
        if (move.getMoveType() == MoveType.SURRENDER) {
            finished = true;
            this.gameResultMessage = String.format(
                    "Gracz %s poddał się.\nWygrywa %s!",
                    move.getStone(),
                    move.getStone().opposite()
            );
            System.out.println("Gracz " + move.getStone() + " poddał się.");
            return; // game finished, move handled
        }

        if (move.getMoveType() == MoveType.PASS) {
            // double pass = finish game
            if (lastMoveType == MoveType.PASS) {
                this.getGameResult();
                finished = true;
                this.gameResultMessage = calculateScoreResult(); // Wywołujemy nową metodę prywatną
                System.out.println("Koniec gry (2x Pass). Wynik: " + this.gameResultMessage);
                System.out.println("Obaj gracze spasowali. Koniec gry.");
            } else {
                lastMoveType = MoveType.PASS;
                currentPlayer = currentPlayer.opposite();
            }
            // end of handling move for pass
            return;
        }

        lastMoveType = MoveType.PLAY;

        // move handling
        for (Rule rule : rules) {
            rule.validate(this, move);
        }

        board.placeStone(
                move.getX(),
                move.getY(),
                move.getStone()
        );

        int capturedCount = ChainAnalyzer.removeDeadOpponentStones(
                board,
                move.getX(),
                move.getY(),
                currentPlayer.opposite()
        );

        if (capturedCount > 0) {
            addPrisoners(currentPlayer, capturedCount);
        }

        saveHistory();

        currentPlayer = currentPlayer.opposite();
    }

    private String calculateScoreResult() {
        Map<Stone, Integer> territoryScore = TerritoryCalculator.calculateTerritory(board);

        int totalBlack = territoryScore.get(Stone.BLACK) + prisonersBlack;
        int totalWhite = territoryScore.get(Stone.WHITE) + prisonersWhite;

        return String.format(
                "Koniec Gry!\nCZARNY: %d (Teren: %d, Jeńcy: %d)\nBIAŁY: %d (Teren: %d, Jeńcy: %d)\nZwycięzca: %s",
                totalBlack, territoryScore.get(Stone.BLACK), prisonersBlack,
                totalWhite, territoryScore.get(Stone.WHITE), prisonersWhite,
                (totalBlack > totalWhite ? "CZARNY" : (totalWhite > totalBlack ? "BIAŁY" : "REMIS"))
        );
    }

    private void addPrisoners(Stone capturer, int count) {
        if (capturer == Stone.BLACK) {
            prisonersBlack += count;
        } else {
            prisonersWhite += count;
        }
    }

    public String getGameResult() {
        return this.gameResultMessage;
    }



    public boolean isFinished() {
        return finished;
    }

    public boolean isBoardStateRepeated(Stone[][] newGrid) {
        for (Stone[][] historicGrid : history) {
            if (Arrays.deepEquals(historicGrid, newGrid)) {
                return true;
            }
        }
        return false;
    }

    private void saveHistory() {
        int size = board.getSize();
        Stone[][] snapshot = new Stone[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                snapshot[x][y] = board.getStone(x, y);
            }
        }
        history.add(snapshot);
    }

    public List<Move> getLegalMoves(Stone stone) {
        int size = board.getSize();
        List<Move> legalMoves = new ArrayList<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                for (Rule rule : rules) {
                    Move move = new Move(x, y, stone);
                    if (rule.check(this, move)) {
                        legalMoves.add(move);
                    }
                }
            }
        }
        return legalMoves;
    }
}
