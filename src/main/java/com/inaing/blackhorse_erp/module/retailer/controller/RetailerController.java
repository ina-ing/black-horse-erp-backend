package com.inaing.blackhorse_erp.module.retailer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.common.dto.list.ListDtoWithAnalytics;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.usecase.IRetailerUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/retailer")
@RequiredArgsConstructor
public class RetailerController {

    private final IRetailerUsecase retailerUsecase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RetailerResponseDto> create(@Valid @RequestBody RetailerCreationRequestDto request) {
        return ApiResponse.created("Retailer created", retailerUsecase.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ApiResponse<ListDtoWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto>> getAllRetailers(
            @RequestParam(required = false) String code, Pageable pageable) {
        return ApiResponse.ok(retailerUsecase.getAllRetailers());
    }

    @GetMapping("/{identifier}")
    public ApiResponse<RetailerResponseDto> getByCode(@PathVariable String identifier) {
        return ApiResponse.ok(retailerUsecase.getByIdentifier(identifier));
    }

}
