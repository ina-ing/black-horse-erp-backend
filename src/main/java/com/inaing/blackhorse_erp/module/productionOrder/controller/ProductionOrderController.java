package com.inaing.blackhorse_erp.module.productionOrder.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.productionOrder.dto.request.ProductionOrderCreationRequstDto;
import com.inaing.blackhorse_erp.module.productionOrder.dto.response.ProductionOrderResponseDto;
import com.inaing.blackhorse_erp.module.productionOrder.usecase.IProductionOrderUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/production-order")
@RequiredArgsConstructor
public class ProductionOrderController {

    private final IProductionOrderUsecases productionOrderUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductionOrderResponseDto> create(
            @Valid @RequestBody ProductionOrderCreationRequstDto request) {
        return ApiResponse.created("Production order created", productionOrderUsecases.create(request));
    }

    @PutMapping("/{identifier}/accept")
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN')")
    public ApiResponse<ProductionOrderResponseDto> accept(@PathVariable String identifier) {
        return ApiResponse.ok("Production order accepted", productionOrderUsecases.accept(identifier));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<ProductionOrderResponseDto> getByIdentifier(@PathVariable String identifier) {
        return ApiResponse.ok(productionOrderUsecases.getByIdentifier(identifier));
    }

    @GetMapping
    public ApiResponse<List<ProductionOrderResponseDto>> getAll() {
        return ApiResponse.ok(productionOrderUsecases.getAll());
    }
    

}
