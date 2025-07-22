package br.com.feedhub.interfaces.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.FOUND)
public class ResourceFoundException extends  RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceFoundException(String message) {
        super(message);
    }

}
