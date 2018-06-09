package com.realtime.statistics.exception;

import com.realtime.statistics.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(ValidationException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(exception.getErrorCode(), exception.getErrorMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(ErrorCode.UNEXPECTED_ERROR.getCode(),
                ErrorCode.UNEXPECTED_ERROR.getMessage());
    }
}