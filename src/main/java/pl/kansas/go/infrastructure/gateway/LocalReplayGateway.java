package pl.kansas.go.infrastructure.gateway;

import pl.kansas.go.domain.model.Game;
import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class LocalReplayGateway implements GameGateway {

    private Game game;
    private final List<Move> moves;
    private final Supplier<Game> gameSupplier;
    private int currentStep = 0;
    private Runnable onStateChanged;

    public LocalReplayGateway(Supplier<Game> gameSupplier, List<Move> moves) {
        this.gameSupplier = gameSupplier;
        this.game = gameSupplier.get();
        this.moves = moves;
    }

    public void next() {
        if (currentStep < moves.size()) {
            Move move = moves.get(currentStep);
            game.applyMove(move);
            currentStep++;
            notifyStateChanged();
        }
    }

    public void prev() {
        // rebuild game from scratch
        if (currentStep > 0) {
            currentStep--;
            this.game = gameSupplier.get();
            for (int i = 0; i < currentStep; i++) {
                game.applyMove(moves.get(i));
            }
            notifyStateChanged();
        }
    }

    @Override
    public BoardViewModel getBoardViewModel() {
        return mapBoard(game.getBoard());
    }

    @Override
    public void makeMove(int x, int y) {
    }

    @Override
    public Stone getMyStone() {
        return Stone.BLACK; // Dummy
    }

    @Override
    public Stone getCurrentPlayer() {
        return game.getCurrentPlayer();
    }

    @Override
    public void setOnStateChanged(Runnable listener) {
        this.onStateChanged = listener;
    }

    @Override
    public void pass() {
    }

    @Override
    public void surrender() {
    }

    @Override
    public boolean isFinished() {
        return currentStep >= moves.size();
    }

    @Override
    public String getGameResult() {
        return "Replay Mode";
    }

    @Override
    public List<UUID> fetchGameList() {
        return Collections.emptyList();
    }

    @Override
    public List<Move> fetchGameMoves(UUID gameId) {
        return Collections.emptyList();
    }

    private void notifyStateChanged() {
        if (onStateChanged != null)
            onStateChanged.run();
    }

    private BoardViewModel mapBoard(pl.kansas.go.domain.model.Board board) {
        int size = board.getSize();
        BoardViewModel.Cell[][] cells = new BoardViewModel.Cell[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Stone s = board.getStone(x, y);
                cells[x][y] = (s == null)
                        ? BoardViewModel.Cell.EMPTY
                        : (s == Stone.BLACK
                                ? BoardViewModel.Cell.BLACK
                                : BoardViewModel.Cell.WHITE);
            }
        }
        return new BoardViewModel(size, cells);
    }
}
