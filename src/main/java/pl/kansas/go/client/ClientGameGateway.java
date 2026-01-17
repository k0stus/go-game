package pl.kansas.go.client;

import pl.kansas.go.gui.dto.BoardViewModel;
import pl.kansas.go.domain.model.Stone;

/**
 * Abstrakcja klienta gry Go.
 *
 * <p>
 * Oddziela warstwę GUI od szczegółów komunikacji sieciowej.
 * Presenter komunikuje się wyłącznie przez ten interfejs.
 */
public interface ClientGameGateway {

    /**
     * Wysyła ruch gracza do serwera.
     *
     * @param x współrzędna X
     * @param y współrzędna Y
     */
    void sendMove(int x, int y);

    /**
     * Pobiera aktualny stan planszy od serwera.
     *
     * @return model widoku planszy
     */
    BoardViewModel getBoard();

    /**
     * Zwraca kolor przypisany temu klientowi.
     *
     * @return kolor gracza
     */
    Stone getPlayerColor();
}
