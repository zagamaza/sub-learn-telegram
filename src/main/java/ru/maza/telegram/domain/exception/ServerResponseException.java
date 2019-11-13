package ru.maza.telegram.domain.exception;

import org.springframework.http.HttpStatus;

public class ServerResponseException extends DomainException {

    {
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ServerResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerResponseException(String message) {
        super(message);
    }

    public ServerResponseException(Throwable cause) {
        super(cause);
    }

}
