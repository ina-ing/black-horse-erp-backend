package com.inaing.blackhorse_erp.module.retailer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.common.dto.list.ListDtoWithAnalytics;
import com.inaing.blackhorse_erp.module.employee.dto.EmployeeResponseDto;
import com.inaing.blackhorse_erp.module.retailer.dto.analytics.RetailerListAnalyticsDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerCreationRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.request.RetailerUpdateRequestDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerDetailsResponseDto;
import com.inaing.blackhorse_erp.module.retailer.dto.response.RetailerResponseDto;
import com.inaing.blackhorse_erp.module.retailer.usecase.IRetailerUsecase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/retailer")
@RequiredArgsConstructor
public class RetailerController {

    private final IRetailerUsecase retailerUsecase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ApiResponse<RetailerResponseDto> create(@Valid @RequestBody RetailerCreationRequestDto request) {
        return ApiResponse.created("Retailer created", retailerUsecase.create(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ListDtoWithAnalytics<RetailerListAnalyticsDto, RetailerResponseDto>> getAllRetailers(
            @RequestParam(required = false) String code, Pageable pageable) {
        return ApiResponse.ok(retailerUsecase.getAllRetailers());
    }

    @GetMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES', 'RETAILER')")
    public ApiResponse<RetailerDetailsResponseDto> getByCode(@PathVariable String identifier) {
        return ApiResponse.ok(retailerUsecase.getByIdentifier(identifier));
    }

    @GetMapping("/assigned-retailers")
    @PreAuthorize("hasRole('SALES')")
    public ApiResponse<List<RetailerResponseDto>> getAssignedRetailers() {
        return ApiResponse.ok(retailerUsecase.getAssignedRetailers());
    }

    @GetMapping("/assigned-salesman")
    @PreAuthorize("hasRole('RETAILER')")
    public ApiResponse<EmployeeResponseDto> getAssignedSalesman() {
        return ApiResponse.ok(retailerUsecase.getAssignedSalesman());
    }

    @PutMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ApiResponse<RetailerResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody RetailerUpdateRequestDto request) {
        return ApiResponse.ok("Retailer updated", retailerUsecase.update(identifier, request));
    }

}
