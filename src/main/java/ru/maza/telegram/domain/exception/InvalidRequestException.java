package ru.maza.telegram.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ServerResponseException {

    {
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(String message) {
        super(message);
    }


    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

}
