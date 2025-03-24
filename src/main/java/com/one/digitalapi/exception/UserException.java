package com.one.digitalapi.exception;

import java.time.LocalDateTime;

public class UserException extends RuntimeException {

    public UserException(String message) {
        super(message);
    }

}