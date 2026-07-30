package com.inaing.blackhorse_erp.exception.exceptions;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

    private final String code;

    public BusinessRuleException(String code, String message) {
        super(message);
        this.code = code;
    }
}
