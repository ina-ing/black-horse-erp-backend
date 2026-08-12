package com.inaing.blackhorse_erp.common.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodeType {

    ADMIN("AD", 6),
    SALES("SL", 6),
    WAREHOUSE("WH", 6),
    FACTORY("FC", 6),
    RETAILER("RET", 6),

    ORDER("OD", 6),
    RETURN("RE", 6),
    SUPPLY("SU", 6),
    PRODUCTION("PR", 6),
    PRODUCTION_ORDER("PO", 6);

    private final String prefix;
    private final int length;

}
