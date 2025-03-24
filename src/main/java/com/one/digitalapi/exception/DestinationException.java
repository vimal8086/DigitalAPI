package com.one.digitalapi.exception;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class DestinationException extends RuntimeException {

    private final String errorCode;
    private final LocalDateTime timestamp;

    public DestinationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
}
