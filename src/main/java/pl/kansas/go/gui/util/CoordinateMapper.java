package pl.kansas.go.gui.util;

/**
 * Narzędzie odpowiedzialne za mapowanie współrzędnych pomiędzy:
 * <ul>
 *     <li>pikselami (Canvas)</li>
 *     <li>logiką planszy (indeksy pól)</li>
 * </ul>
 *
 * <p>
 * Klasa jest całkowicie niezależna od JavaFX i może być
 * testowana jednostkowo.
 */
public class CoordinateMapper {

    private final int boardSize;
    private final double canvasSize;
    private final double margin;
    private final double cellSize;

    /**
     * Tworzy mapper współrzędnych.
     *
     * @param boardSize  rozmiar planszy (np. 9, 13, 19)
     * @param canvasSize rozmiar Canvas (szerokość = wysokość)
     * @param margin     margines wokół planszy
     */
    public CoordinateMapper(int boardSize, double canvasSize, double margin) {
        this.boardSize = boardSize;
        this.canvasSize = canvasSize;
        this.margin = margin;
        this.cellSize = (canvasSize - 2 * margin) / (boardSize - 1);
    }

    /**
     * Zamienia współrzędne pikselowe na indeks planszy.
     *
     * @param pixelX współrzędna X w pikselach
     * @param pixelY współrzędna Y w pikselach
     * @return tablica [x, y] lub null jeśli poza planszą
     */
    public int[] toBoardCoordinate(double pixelX, double pixelY) {
        int x = (int) Math.round((pixelX - margin) / cellSize);
        int y = (int) Math.round((pixelY - margin) / cellSize);

        if (x < 0 || x >= boardSize || y < 0 || y >= boardSize) {
            return null;
        }
        return new int[]{x, y};
    }

    /**
     * Zamienia indeks planszy na pozycję w pikselach.
     *
     * @param index indeks pola
     * @return pozycja w pikselach
     */
    public double toPixel(int index) {
        return margin + index * cellSize;
    }

    /**
     * Zwraca rozmiar pojedynczej komórki.
     *
     * @return rozmiar pola w pikselach
     */
    public double getCellSize() {
        return cellSize;
    }
}
