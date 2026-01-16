package pl.kansas.go.gui.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.kansas.go.application.GameFactory;
import pl.kansas.go.application.GameService;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe GamePresenter.
 */
class GamePresenterTest {

    private GamePresenter presenter;

    @BeforeEach
    void setUp() {
        GameService service = new GameService(new GameFactory());
        presenter = new GamePresenter(service);
    }

    @Test
    void shouldStartWithBlackPlayer() {
        assertEquals(Stone.BLACK, presenter.getCurrentPlayer());
    }

    @Test
    void shouldPlaceStoneAndSwitchPlayer() {
        presenter.makeMove(4, 4);

        assertEquals(Stone.WHITE, presenter.getCurrentPlayer());

        BoardViewModel board = presenter.getBoardViewModel();
        assertEquals(BoardViewModel.Cell.BLACK, board.getCell(4, 4));
    }

    @Test
    void shouldRejectMoveOnOccupiedField() {
        presenter.makeMove(4, 4);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> presenter.makeMove(4, 4)
        );

        assertTrue(ex.getMessage().toLowerCase().contains("zajęte"));
    }
}
