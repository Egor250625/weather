package by.egorivanov.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SignUpException extends RuntimeException {
    public SignUpException(String message) {
        super(message);
    }
}
