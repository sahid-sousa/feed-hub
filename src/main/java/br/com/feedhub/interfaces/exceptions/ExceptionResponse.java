package br.com.feedhub.interfaces.exceptions;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ExceptionResponse (
        LocalDateTime timestamp,
        String message,
        String details
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
