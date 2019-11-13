package ru.maza.telegram.domain.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {

    {
        this.httpStatus = HttpStatus.NOT_FOUND;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
