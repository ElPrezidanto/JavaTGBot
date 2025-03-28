package backend.academy.exceptions;

public class NotFound extends RuntimeException {
    public final String description;

    public NotFound(String message, String description) {
        super(message);
        this.description = description;
    }
}
