package com.inaing.blackhorse_erp.module.supply.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.production.dto.request.ProductionRequestDto;
import com.inaing.blackhorse_erp.module.production.dto.response.ProductionResponseDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyCreationRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyItemsUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.request.SupplyStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.supply.dto.response.SupplyResponseDto;
import com.inaing.blackhorse_erp.module.supply.usecase.ISupplyUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/supply")
@RequiredArgsConstructor
public class SupplyController {

    private final ISupplyUsecases supplyUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN')")
    public ApiResponse<SupplyResponseDto> create(@Valid @RequestBody SupplyCreationRequestDto request) {
        return ApiResponse.created("Supply Created", supplyUsecases.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN', 'WAREHOUSE')")
    public ApiResponse<List<SupplyResponseDto>> getAll() {
        return ApiResponse.ok(supplyUsecases.getAll());
    }

    @GetMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('FACTORY', 'ADMIN', 'WAREHOUSE')")
    public ApiResponse<SupplyResponseDto> getByIdentifier(@PathVariable String identifier) {
        return ApiResponse.ok(supplyUsecases.getByIdentifier(identifier));
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE')")
    public ApiResponse<SupplyResponseDto> updateStatus(@PathVariable String id,
            @RequestBody SupplyStatusUpdateRequestDto request) {
        return ApiResponse.ok(supplyUsecases.updateStatus(id, request));
    }

    @PutMapping("/{identifier}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<SupplyResponseDto> update(@PathVariable String identifier,
            @Valid @RequestBody SupplyItemsUpdateRequestDto request) {
        return ApiResponse.ok(supplyUsecases.updateItems(identifier, request));
    }
}
