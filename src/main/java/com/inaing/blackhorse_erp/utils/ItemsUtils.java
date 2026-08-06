package com.inaing.blackhorse_erp.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.inaing.blackhorse_erp.common.dto.request.VariantQuantityRequestDto;

public class ItemsUtils {
    
    public static <T extends VariantQuantityRequestDto> Map<String, Integer> mergeItems(List<T> items) {

        Map<String, Integer> merged = new LinkedHashMap<>();

        for (T item : items) {
            merged.merge(
                    item.variantSizeId(),
                    item.quantity(),
                    (existing, incoming) -> existing + incoming);
        }

        return merged;
    }
}
