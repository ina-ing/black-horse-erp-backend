package com.inaing.blackhorse_erp.common.dto.pagination;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.inaing.blackhorse_erp.common.dto.request.PageableRequest;

public final class PageableFactory {

    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    private PageableFactory() {
    }

    public static Pageable from(PageableRequest request, Map<String, String> sortAllowlist, Sort defaultSort) {
        int page = request.getPage() == null || request.getPage() < 0 ? 0 : request.getPage();
        int size = request.getSize() == null || request.getSize() < 1
                ? DEFAULT_SIZE
                : Math.min(request.getSize(), MAX_SIZE);
        return PageRequest.of(page, size, resolveSort(request, sortAllowlist, defaultSort));
    }

    private static Sort resolveSort(PageableRequest request, Map<String, String> sortAllowlist, Sort defaultSort) {
        String sortBy = request.getSortBy();
        if (sortBy == null || !sortAllowlist.containsKey(sortBy)) {
            return defaultSort;
        }
        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDir())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        String property = sortAllowlist.get(sortBy);
        return Sort.by(new Sort.Order(direction, property, Sort.NullHandling.NULLS_LAST));
    }
}
