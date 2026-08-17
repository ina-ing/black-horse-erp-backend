package com.inaing.blackhorse_erp.module.production.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionRequestDto;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionQueryParams;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionListResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.production.dto.response.analytics.ProductionBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.production.usecase.IProductionUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/production")
@RequiredArgsConstructor
public class ProductionController {

    private final IProductionUsecases productionUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN')")
    public ApiResponse<ProductionResponseDto> create(@Valid @RequestBody ProductionRequestDto request) {
        return ApiResponse.created("Production created", productionUsecases.create(request));
    }

    @GetMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN')")
    public ApiResponse<ProductionResponseDto> getByIdentifier(@PathVariable String identifier) {
        return ApiResponse.ok(productionUsecases.getByIdentifier(identifier));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN')")
    public ApiResponse<PagedListWithAnalytics<ProductionBasicAnalyticsDto, ProductionListResponseDto>> getAll(
            @ModelAttribute ProductionQueryParams params) {
        return ApiResponse.ok(productionUsecases.getAll(params));
    }

    @GetMapping("/factory/{identifier}")
    public ApiResponse<List<ProductionResponseDto>> getByFactory(@PathVariable String identifier) {
        return ApiResponse.ok(productionUsecases.getByFactoryId(identifier));
    }
    
    @PutMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<ProductionResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody ProductionRequestDto request) {
        return ApiResponse.ok(productionUsecases.update(identifier, request));
    }
}
