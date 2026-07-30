package com.inaing.blackhorse_erp.common.dto.list;

import java.util.List;

public record ListDtoWithAnalytics<T, Q>(T analytics, List<Q> list) {

    public static <T, Q> ListDtoWithAnalytics<T, Q> of(T analytics, List<Q> list){
        return new ListDtoWithAnalytics<>(analytics, list);
    }
} 