package com.realtime.statistics.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationException extends RuntimeException {

    private Integer errorCode;

    private String errorMessage;

    public ValidationException(ErrorCode errorCode) {
        super(errorCode.getCode() + "-" + errorCode.getMessage());

        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }
}