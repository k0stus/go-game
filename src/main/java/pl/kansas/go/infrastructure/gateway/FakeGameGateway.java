package pl.kansas.go.infrastructure.gateway;

import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

public class FakeGameGateway implements GameGateway {

    public BoardViewModel board;
    public Stone myStone;
    public Stone currentPlayer;
    Runnable listener;

    @Override
    public void setOnStateChanged(Runnable r) {
        this.listener = r;
    }

    public void fireStateChanged() {
        if (listener != null) listener.run();
    }

    @Override
    public BoardViewModel getBoardViewModel() {
        return board;
    }

    @Override
    public void makeMove(int x, int y) {
        // no-op
    }

    @Override
    public Stone getMyStone() {
        return myStone;
    }

    @Override
    public Stone getCurrentPlayer() {
        return currentPlayer;
    }
}

