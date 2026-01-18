package pl.kansas.go.gui.controller;

import org.junit.jupiter.api.Test;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;
import pl.kansas.go.gui.presenter.GamePresenter;
import pl.kansas.go.infrastructure.gateway.FakeGameGateway;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    @Test
    void hasBoard_returnsFalse_whenBoardIsNull() {
        FakeGameGateway gateway = new FakeGameGateway();
        GamePresenter presenter = new GamePresenter(gateway);

        assertFalse(presenter.hasBoard());
    }

    @Test
    void hasBoard_returnsTrue_whenBoardExists() {
        FakeGameGateway gateway = new FakeGameGateway();
        gateway.board = new BoardViewModel(9, new BoardViewModel.Cell[9][9]);

        GamePresenter presenter = new GamePresenter(gateway);

        assertTrue(presenter.hasBoard());
    }

    @Test
    void presenter_notifies_listener_on_state_change() {
        FakeGameGateway gateway = new FakeGameGateway();
        GamePresenter presenter = new GamePresenter(gateway);

        AtomicBoolean called = new AtomicBoolean(false);
        presenter.setOnStateChanged(() -> called.set(true));

        gateway.fireStateChanged();

        assertTrue(called.get());
    }

    @Test
    void isMyTurn_returnsTrue_when_myStone_equals_currentPlayer() {
        FakeGameGateway gateway = new FakeGameGateway();
        gateway.myStone = Stone.BLACK;
        gateway.currentPlayer = Stone.BLACK;

        GamePresenter presenter = new GamePresenter(gateway);

        assertTrue(presenter.isMyTurn());
    }
}