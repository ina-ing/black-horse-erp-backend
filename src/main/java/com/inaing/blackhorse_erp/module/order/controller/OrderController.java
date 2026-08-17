package com.inaing.blackhorse_erp.module.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFulfillmentRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderQueryParams;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderListResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.analytics.OrderBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.order.usecase.IOrderUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderUsecases orderUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','SALES','RETAILER')")
    public ApiResponse<OrderResponseDto> create(@Valid @RequestBody OrderCreationRequestDto request) {
        return ApiResponse.created("Order created", orderUsecases.create(request));
    }

    @GetMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES','RETAILER','WAREHOUSE','FACTORY')")
    public ApiResponse<OrderWithStatusHistoryResponseDto> getByCode(@PathVariable String identifier) {
        return ApiResponse.ok(orderUsecases.getByIdentifier(identifier));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES','RETAILER','WAREHOUSE','FACTORY')")
    public ApiResponse<PagedListWithAnalytics<OrderBasicAnalyticsDto, OrderListResponseDto>> getAll(
            @ModelAttribute OrderQueryParams params) {
        return ApiResponse.ok(orderUsecases.getAll(params));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES','RETAILER')")
    public ApiResponse<OrderResponseDto> update(@PathVariable String id,
            @Valid @RequestBody OrderUpdateRequestDto request) {
        return ApiResponse.ok(orderUsecases.update(id, request));
    }

    @PostMapping("/{id}/fulfill")
    @PreAuthorize("hasAnyRole('ADMIN','WAREHOUSE')")
    public ApiResponse<OrderResponseDto> fulfill(@PathVariable String id,
            @Valid @RequestBody OrderFulfillmentRequestDto request) {
        return ApiResponse.ok(orderUsecases.fulfill(id, request));
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES','RETAILER','WAREHOUSE')")
    public ApiResponse<OrderResponseDto> updateStatus(@PathVariable String id,
            @RequestBody OrderStatusUpdateRequestDto request) {
        return ApiResponse.ok(orderUsecases.updateStatus(id, request));
    }
}
