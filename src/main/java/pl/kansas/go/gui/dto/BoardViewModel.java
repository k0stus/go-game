package pl.kansas.go.gui.dto;

/**
 * ViewModel reprezentujący planszę gry na potrzeby GUI.
 * <p>
 * Klasa jest niemutowalna i niezależna od warstwy domenowej.
 */
public class BoardViewModel {

    private final int size;
    private final Cell[][] cells;

    /**
     * Tworzy ViewModel planszy.
     *
     * @param size  rozmiar planszy
     * @param cells tablica komórek (kopiowana defensywnie)
     */
    public BoardViewModel(int size, Cell[][] cells) {
        this.size = size;
        this.cells = new Cell[size][size];

        for (int x = 0; x < size; x++) {
            System.arraycopy(cells[x], 0, this.cells[x], 0, size);
        }
    }

    public BoardViewModel(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
    }

    /**
     * Zwraca rozmiar planszy.
     *
     * @return rozmiar planszy
     */
    public int getSize() {
        return size;
    }

    /**
     * Zwraca stan pojedynczego pola planszy.
     *
     * @param x współrzędna X
     * @param y współrzędna Y
     * @return stan pola
     */
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /**
     * Reprezentacja stanu pola z perspektywy GUI.
     */
    public enum Cell {
        EMPTY,
        BLACK,
        WHITE,
        DEAD_BLACK,
        DEAD_WHITE
    }
}
