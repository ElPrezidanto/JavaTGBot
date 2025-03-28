package backend.academy.scrapper.data.source.remote.stackoverflow;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

public class MyCustomRuntimeException extends RuntimeException {
    public MyCustomRuntimeException(HttpStatusCode statusCode, HttpHeaders headers) {
        super("код - " + statusCode + "\nheaders- " + headers);
    }
}
