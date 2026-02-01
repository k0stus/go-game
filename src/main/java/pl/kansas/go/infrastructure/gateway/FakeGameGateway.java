package pl.kansas.go.infrastructure.gateway;

import pl.kansas.go.domain.model.Move;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FakeGameGateway implements GameGateway {

    public BoardViewModel board;
    public Stone myStone;
    public Stone currentPlayer;
    Runnable listener;

    @Override
    public void setOnStateChanged(Runnable r) {
        this.listener = r;
    }

    @Override
    public void pass() {

    }

    @Override
    public void surrender() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public String getGameResult() {
        return "";
    }

    public void fireStateChanged() {
        if (listener != null)
            listener.run();
    }

    @Override
    public BoardViewModel getBoardViewModel() {
        return board;
    }

    @Override
    public void makeMove(int x, int y) {

    }

    @Override
    public Stone getMyStone() {
        return myStone;
    }

    @Override
    public Stone getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public List<UUID> fetchGameList() {
        return Collections.emptyList();
    }

    @Override
    public List<Move> fetchGameMoves(UUID gameId) {
        return Collections.emptyList();
    }
}
