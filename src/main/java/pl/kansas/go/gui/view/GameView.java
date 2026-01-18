package pl.kansas.go.gui.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import pl.kansas.go.domain.model.Stone;
import pl.kansas.go.gui.dto.BoardViewModel;
import pl.kansas.go.gui.util.CoordinateMapper;

import java.util.function.BiConsumer;

/**
 * Widok gry Go odpowiedzialny wyłącznie za renderowanie
 * oraz przekazywanie zdarzeń do kontrolera.
 */
public class GameView extends BorderPane {

    private static final int CANVAS_SIZE = 600;
    private static final int PADDING = 40;

    private final Canvas canvas;
    private final StatusBar statusBar;

    private BiConsumer<Integer, Integer> onCellClicked;
    private CoordinateMapper mapper;

    public GameView() {
        this.canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        this.statusBar = new StatusBar();

        setCenter(canvas);
        setBottom(statusBar);

        registerMouseHandler();
    }

    /**
     * Rejestruje obsługę kliknięć w planszę.
     */
    private void registerMouseHandler() {
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (mapper == null || onCellClicked == null) {
                return;
            }
            int[] coord = mapper.toBoardCoordinate(event.getX(), event.getY());
            if (coord != null) {
                onCellClicked.accept(coord[0], coord[1]);
            }
        });
    }

    /**
     * Ustawia obsługę kliknięcia w pole planszy.
     *
     * @param handler handler kliknięcia
     */
    public void setOnCellClicked(BiConsumer<Integer, Integer> handler) {
        this.onCellClicked = handler;
    }

    /**
     * Aktualizuje planszę.
     *
     * @param board model widoku planszy
     */
    public void updateBoard(BoardViewModel board) {
        System.out.println("GAMEVIEW: updateBoard()");
        this.mapper = new CoordinateMapper(
                board.getSize(),
                CANVAS_SIZE,
                PADDING
        );
        drawBoard(board);
    }

    /**
     * Aktualizuje pasek statusu.
     *
     * @param text tekst statusu
     */
    public void setStatus(String text) {
        statusBar.showMessage(text);
    }

    private void drawBoard(BoardViewModel board) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

        int size = board.getSize();
        double cell = mapper.getCellSize();

        gc.setStroke(Color.BLACK);

        for (int i = 0; i < size; i++) {
            double p = mapper.toPixel(i);
            gc.strokeLine(
                    mapper.toPixel(0), p,
                    mapper.toPixel(size - 1), p
            );
            gc.strokeLine(
                    p, mapper.toPixel(0),
                    p, mapper.toPixel(size - 1)
            );
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                drawStone(gc, board.getCell(x, y),
                        mapper.toPixel(x),
                        mapper.toPixel(y),
                        cell / 2 - 2);
            }
        }
    }

    private void drawStone(GraphicsContext gc,
                           BoardViewModel.Cell cell,
                           double cx,
                           double cy,
                           double radius) {
        if (cell == BoardViewModel.Cell.EMPTY) return;

        gc.setFill(cell == BoardViewModel.Cell.BLACK
                ? Color.BLACK
                : Color.WHITE);

        gc.fillOval(cx - radius, cy - radius,
                radius * 2, radius * 2);
        gc.strokeOval(cx - radius, cy - radius,
                radius * 2, radius * 2);
    }

    public void setOnPassAction(Runnable action) {
        statusBar.setOnPassAction(action);
    }

    public void setOnSurrenderAction(Runnable action) {
        statusBar.setOnSurrenderAction(action);
    }

    public void setControlsDisabled(boolean disabled) {
        statusBar.setButtonsDisabled(disabled);
    }

}
