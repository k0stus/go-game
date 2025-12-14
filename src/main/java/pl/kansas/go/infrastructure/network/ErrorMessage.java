package pl.kansas.go.infrastructure.network;

public class ErrorMessage implements Message {

    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
