package com.pru.starter.model.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {
    GENERIC_ERROR_CODE("0000"),
    VALIDATION_ERROR_CODE("0002");

    private final String code;
}
