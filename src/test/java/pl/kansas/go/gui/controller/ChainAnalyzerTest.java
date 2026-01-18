package pl.kansas.go.gui.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.kansas.go.domain.model.Board;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.domain.utils.ChainAnalyzer;

import static org.junit.jupiter.api.Assertions.*;

public class ChainAnalyzerTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(9);
    }

    @Test
    @DisplayName("Pojedynczy kamień na środku powinien mieć oddechy")
    void testSingleStoneLiberties() {
        board.placeStone(4, 4, Stone.BLACK);

        boolean hasLiberties = ChainAnalyzer.hasLiberties(board, 4, 4);

        assertTrue(hasLiberties, "Samotny kamień powinien mieć 4 oddechy");
    }

    @Test
    @DisplayName("Kamień całkowicie otoczony nie powinien mieć oddechów")
    void testSurroundedStoneNoLiberties() {
        board.placeStone(1, 1, Stone.WHITE);

        board.placeStone(0, 1, Stone.BLACK);
        board.placeStone(2, 1, Stone.BLACK);
        board.placeStone(1, 0, Stone.BLACK);
        board.placeStone(1, 2, Stone.BLACK);

        boolean hasLiberties = ChainAnalyzer.hasLiberties(board, 1, 1);

        assertFalse(hasLiberties, "Otoczony kamień nie powinien mieć oddechów");
    }

    @Test
    @DisplayName("Grupa dwóch kamieni dzieli oddechy")
    void testGroupSharingLiberties() {
        board.placeStone(2, 2, Stone.BLACK);
        board.placeStone(2, 3, Stone.BLACK);

        board.placeStone(1, 2, Stone.WHITE);
        board.placeStone(3, 2, Stone.WHITE);
        board.placeStone(2, 1, Stone.WHITE);

        boolean hasLiberties = ChainAnalyzer.hasLiberties(board, 2, 2);

        assertTrue(hasLiberties, "Grupa żyje, dopóki chociaż jeden kamień ma oddech");
    }

    @Test
    @DisplayName("Zbijanie: Powinien usunąć martwą grupę przeciwnika")
    void testCaptureDeadStones() {
        board.placeStone(1, 1, Stone.WHITE); // Ofiara

        board.placeStone(0, 1, Stone.BLACK);
        board.placeStone(2, 1, Stone.BLACK);
        board.placeStone(1, 0, Stone.BLACK);

        board.placeStone(1, 2, Stone.BLACK); // Ruch zabijający

        int captured = ChainAnalyzer.removeDeadOpponentStones(board, 1, 2, Stone.WHITE);

        assertEquals(1, captured, "Powinien zostać zbity 1 kamień");
        assertTrue(board.isEmpty(1, 1), "Pole po zbitym kamieniu powinno być puste");
    }

    @Test
    @DisplayName("Samobójstwo: Nie powinno usunąć kamieni, jeśli grupa żyje")
    void testDoNotCaptureLivingStones() {
        board.placeStone(1, 1, Stone.WHITE);
        board.placeStone(1, 0, Stone.BLACK); // Czarny tylko straszy

        board.placeStone(5, 5, Stone.BLACK);
        int captured = ChainAnalyzer.removeDeadOpponentStones(board, 5, 5, Stone.WHITE);

        assertEquals(0, captured, "Nic nie powinno zostać zbite");
        assertFalse(board.isEmpty(1, 1), "Biały kamień nadal powinien być na planszy");
    }
}
