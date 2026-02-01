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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Widok gry Go odpowiedzialny wyłącznie za renderowanie
 * oraz przekazywanie zdarzeń do kontrolera.
 */
public class GameView extends BorderPane {

    private static final int CANVAS_SIZE = 600;
    private static final int PADDING = 40;

    private final Canvas canvas;
    private final StatusBar statusBar;
    private final MenuBar menuBar;
    private final ToolBar toolBar;
    private final Button btnPrev;
    private final Button btnNext;

    private BiConsumer<Integer, Integer> onCellClicked;
    private Runnable onReplayClicked;
    private Runnable onNextClicked;
    private Runnable onPrevClicked;
    private CoordinateMapper mapper;

    public GameView() {
        this.canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);
        this.statusBar = new StatusBar();
        this.menuBar = createMenuBar();
        this.toolBar = createToolBar();
        this.btnPrev = (Button) toolBar.getItems().get(0);
        this.btnNext = (Button) toolBar.getItems().get(1);

        VBox topContainer = new VBox(menuBar, toolBar);
        setTop(topContainer);
        setCenter(canvas);
        setBottom(statusBar);

        toolBar.setVisible(false);
        toolBar.setManaged(false);

        registerMouseHandler();
    }

    private MenuBar createMenuBar() {
        MenuBar menu = new MenuBar();
        Menu gameMenu = new Menu("Gra");
        MenuItem replayItem = new MenuItem("Odtwórz historię");
        replayItem.setOnAction(e -> {
            if (onReplayClicked != null)
                onReplayClicked.run();
        });
        gameMenu.getItems().add(replayItem);
        menu.getMenus().add(gameMenu);
        return menu;
    }

    private ToolBar createToolBar() {
        Button prev = new Button("Poprzedni");
        Button next = new Button("Następny");

        prev.setOnAction(e -> {
            if (onPrevClicked != null)
                onPrevClicked.run();
        });
        next.setOnAction(e -> {
            if (onNextClicked != null)
                onNextClicked.run();
        });

        return new ToolBar(prev, next);
    }

    public void setOnReplayClicked(Runnable handler) {
        this.onReplayClicked = handler;
    }

    public void setOnNextClicked(Runnable handler) {
        this.onNextClicked = handler;
    }

    public void setOnPrevClicked(Runnable handler) {
        this.onPrevClicked = handler;
    }

    public void setReplayMode(boolean replayMode) {
        toolBar.setVisible(replayMode);
        toolBar.setManaged(replayMode);
        statusBar.setVisible(!replayMode);
        statusBar.setManaged(!replayMode);
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
                PADDING);
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
                    mapper.toPixel(size - 1), p);
            gc.strokeLine(
                    p, mapper.toPixel(0),
                    p, mapper.toPixel(size - 1));
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
        if (cell == BoardViewModel.Cell.EMPTY)
            return;

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
