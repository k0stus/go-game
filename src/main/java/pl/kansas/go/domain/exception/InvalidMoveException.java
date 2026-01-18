package pl.kansas.go.domain.exception;

/**
 * Wyjątek zgłaszany w przypadku nieprawidłowego ruchu w grze Go.
 */
public class InvalidMoveException extends RuntimeException {

    public InvalidMoveException(String message) {
        super(message);
    }
}
