package com.inaing.blackhorse_erp.common.dto.list;

import com.inaing.blackhorse_erp.common.dto.response.PageResponse;

public record PagedListWithAnalytics<A, T>(A analytics, PageResponse<T> page) {

    public static <A, T> PagedListWithAnalytics<A, T> of(A analytics, PageResponse<T> page) {
        return new PagedListWithAnalytics<>(analytics, page);
    }
}
