package com.pru.starter.rest.config;

import com.pru.starter.model.error.ErrorCode;
import com.pru.starter.rest.config.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralExceptionError(Exception exception) {
        return makeResponseError(
            ErrorCode.GENERIC_ERROR_CODE.getCode(),
            String.format("Unpredicted, not caught exception occurred: %s", exception.getMessage()),
            exception
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationError(MethodArgumentNotValidException exception) {
        val fieldError = exception.getBindingResult().getFieldError();
        assert fieldError != null;

        return makeResponseError(
            ErrorCode.VALIDATION_ERROR_CODE.getCode(),
            String.format("%s %s", fieldError.getField(), fieldError.getDefaultMessage()),
            exception
        );
    }

    private ErrorResponse makeResponseError(String code, String message, Exception e) {
        log.warn(String.format("ERROR: code=%s message=%s", code, message), e);
        return ErrorResponse.builder()
            .code(code)
            .message(message)
            .build();
    }
}
