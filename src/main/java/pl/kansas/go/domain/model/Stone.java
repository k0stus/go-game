package pl.kansas.go.domain.model;

public enum Stone {
    BLACK, WHITE;

    public Stone opposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}
