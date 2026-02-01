package pl.kansas.go.infrastructure.gateway;

import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;

/**
 * Abstrakcja dostępu do gry Go z perspektywy GUI.
 *
 * <p>
 * Implementacje:
 * <ul>
 * <li>lokalna (GameService)</li>
 * <li>sieciowa (NetworkClientGateway)</li>
 * </ul>
 */
public interface GameGateway {

    /**
     * Zwraca aktualny stan planszy w postaci ViewModelu.
     *
     * @return model widoku planszy
     */
    BoardViewModel getBoardViewModel();

    /**
     * Wykonuje ruch aktualnego gracza.
     *
     * @param x współrzędna X
     * @param y współrzędna Y
     */
    void makeMove(int x, int y);

    /**
     * Zwraca kamień gracza lokalnego.
     *
     * @return kolor gracza
     */
    Stone getMyStone();

    /**
     * Zwraca kamień gracza, którego jest ruch
     */
    Stone getCurrentPlayer();

    /** Rejestruje listenera wywoływanego przy zmianie stanu gry */
    void setOnStateChanged(Runnable listener);

    /** Wysyła sygnał pominięcia ruchu */
    void pass();

    /** Wysyła sygnał poddania się (SURRENDER) */
    void surrender();

    /** Czy gra jest zakończona? */
    boolean isFinished();

    /** Pobiera komunikat z wynikiem gry (jeśli zakończona) */
    /** Pobiera komunikat z wynikiem gry (jeśli zakończona) */
    String getGameResult();

    // History methods
    java.util.List<java.util.UUID> fetchGameList();

    java.util.List<pl.kansas.go.domain.model.Move> fetchGameMoves(java.util.UUID gameId);
}
