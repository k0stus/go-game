package pl.kansas.go.gui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pl.kansas.go.gui.dto.BoardViewModel;

import java.util.function.BiConsumer;

/**
 * Komponent JavaFX odpowiedzialny za rysowanie planszy Go
 * przy użyciu {@link Canvas}.
 *
 * Klasa:
 * <ul>
 *   <li>nie zna logiki gry</li>
 *   <li>nie zna warstwy domenowej</li>
 *   <li>renderuje wyłącznie {@link BoardViewModel}</li>
 * </ul>
 *
 * Obsługuje:
 * <ul>
 *   <li>rysowanie siatki</li>
 *   <li>rysowanie kamieni</li>
 *   <li>mapowanie kliknięć myszy na współrzędne planszy</li>
 * </ul>
 */
public class BoardCanvas extends Canvas {

    private static final double PADDING = 20.0;

    private BoardViewModel board;
    private BiConsumer<Integer, Integer> onCellClicked;

    /**
     * Tworzy Canvas do rysowania planszy Go.
     *
     * @param sizePx szerokość i wysokość canvasa w pikselach
     */
    public BoardCanvas(double sizePx) {
        super(sizePx, sizePx);
        registerMouseHandler();
    }

    /**
     * Ustawia ViewModel planszy i powoduje jej przerysowanie.
     *
     * @param board aktualny stan planszy
     */
    public void setBoard(BoardViewModel board) {
        this.board = board;
        redraw();
    }

    /**
     * Rejestruje handler kliknięcia w pole planszy.
     *
     * @param handler funkcja przyjmująca współrzędne (x, y)
     */
    public void setOnCellClicked(BiConsumer<Integer, Integer> handler) {
        this.onCellClicked = handler;
    }

    /**
     * Przerysowuje całą planszę.
     */
    public void redraw() {
        GraphicsContext gc = getGraphicsContext2D();
        clear(gc);

        if (board == null) {
            return;
        }

        drawGrid(gc);
        drawStones(gc);
    }

    /**
     * Czyści obszar canvasa.
     */
    private void clear(GraphicsContext gc) {
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Rysuje siatkę planszy.
     */
    private void drawGrid(GraphicsContext gc) {
        int size = board.getSize();
        double cellSize = getCellSize();

        gc.setStroke(Color.BLACK);

        for (int i = 0; i < size; i++) {
            double pos = PADDING + i * cellSize;

            gc.strokeLine(PADDING, pos, PADDING + cellSize * (size - 1), pos);
            gc.strokeLine(pos, PADDING, pos, PADDING + cellSize * (size - 1));
        }
    }

    /**
     * Rysuje kamienie na planszy.
     */
    private void drawStones(GraphicsContext gc) {
        int size = board.getSize();
        double cellSize = getCellSize();
        double radius = cellSize * 0.4;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                BoardViewModel.Cell cell = board.getCell(x, y);
                if (cell == BoardViewModel.Cell.EMPTY) {
                    continue;
                }

                double cx = PADDING + x * cellSize;
                double cy = PADDING + y * cellSize;

                switch (cell) {
                    case BLACK -> gc.setFill(Color.BLACK);
                    case WHITE -> gc.setFill(Color.WHITE);
                    case DEAD_BLACK -> gc.setFill(Color.DARKGRAY);
                    case DEAD_WHITE -> gc.setFill(Color.LIGHTGRAY);
                }

                gc.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);
                gc.strokeOval(cx - radius, cy - radius, radius * 2, radius * 2);
            }
        }
    }

    /**
     * Oblicza rozmiar pojedynczej komórki planszy.
     */
    private double getCellSize() {
        return (getWidth() - 2 * PADDING) / (board.getSize() - 1);
    }

    /**
     * Rejestruje obsługę kliknięć myszy na Canvasie.
     */
    private void registerMouseHandler() {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (board == null || onCellClicked == null) {
                return;
            }

            int x = pixelToCell(event.getX());
            int y = pixelToCell(event.getY());

            if (x >= 0 && y >= 0 &&
                    x < board.getSize() && y < board.getSize()) {
                onCellClicked.accept(x, y);
            }
        });
    }

    /**
     * Zamienia współrzędną pikselową na indeks planszy.
     */
    private int pixelToCell(double pixel) {
        double cellSize = getCellSize();
        return (int) Math.round((pixel - PADDING) / cellSize);
    }
}
