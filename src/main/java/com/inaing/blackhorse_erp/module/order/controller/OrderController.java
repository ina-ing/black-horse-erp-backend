package com.inaing.blackhorse_erp.module.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderCreationRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderFulfillmentRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.request.OrderUpdateRequestDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderResponseDto;
import com.inaing.blackhorse_erp.module.order.dto.response.OrderWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.order.usecase.IOrderUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderUsecases orderUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponseDto> create(@Valid @RequestBody OrderCreationRequestDto request) {
        return ApiResponse.created("Order created", orderUsecases.create(request));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<OrderWithStatusHistoryResponseDto> getByCode(@PathVariable String identifier) {
        return ApiResponse.ok(orderUsecases.getByIdentifier(identifier));
    }

    @GetMapping
    public ApiResponse<List<OrderResponseDto>> getAll() {
        return ApiResponse.ok(orderUsecases.getAll());
    }

    @PutMapping("/{id}")
    public ApiResponse<OrderResponseDto> update(@PathVariable String id,
            @Valid @RequestBody OrderUpdateRequestDto request) {
        return ApiResponse.ok(orderUsecases.update(id, request));
    }

    @PostMapping("/{id}/fulfill")
    public ApiResponse<OrderResponseDto> fulfill(@PathVariable String id,
            @Valid @RequestBody OrderFulfillmentRequestDto request) {
        return ApiResponse.ok(orderUsecases.fulfill(id, request));
    }

    @PutMapping("status/{id}")
    public ApiResponse<OrderResponseDto> updateStatus(@PathVariable String id,
            @RequestBody OrderStatusUpdateRequestDto request) {
        return ApiResponse.ok(orderUsecases.updateStatus(id, request));
    }
}
