package com.realtime.statistics.exception;

import lombok.Getter;

public enum ErrorCode {

    VALIDATION_EMPTY_REQUEST_BODY(1001, "Empty request body"),

    VALIDATION_MISSING_TIMESTAMP(1002, "Missing timestamp field"),

    VALIDATION_MISSING_AMOUNT(1003, "Missing amount field"),

    UNEXPECTED_ERROR(9999, "Internal Server Error");

    @Getter
    private Integer code;

    @Getter
    private String message;

    ErrorCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}