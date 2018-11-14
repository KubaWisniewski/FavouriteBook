package com.app.exceptions;

import lombok.AllArgsConstructor;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@AllArgsConstructor
public class MyException extends RuntimeException {
    private String exceptionMessage;
    private LocalDateTime exceptionDateTime;

    @Override
    public String getMessage() {
        return MessageFormat.format("{0} : {1}", exceptionDateTime, exceptionMessage);
    }
}
