package pl.kansas.go.domain.model;

/**
 * Reprezentuje kolor kamienia w grze Go.
 */
public enum Stone {
    BLACK, WHITE;

    public Stone opposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}
