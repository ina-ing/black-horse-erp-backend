package com.inaing.blackhorse_erp.module.returns.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnCreationRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.returns.usecase.IReturnUsecases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/return")
@RequiredArgsConstructor
public class ReturnController {

    private final IReturnUsecases returnUsecases;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReturnResponseDto> create(@Valid @RequestBody ReturnCreationRequestDto request) {
        return ApiResponse.created("Return created", returnUsecases.create(request));
    }

    @GetMapping("/{identifier}")
    public ApiResponse<ReturnWithStatusHistoryResponseDto> getByCode(@PathVariable String identifier) {
        return ApiResponse.ok(returnUsecases.getByIdentifier(identifier));
    }

    @GetMapping
    public ApiResponse<List<ReturnResponseDto>> getAll() {
        return ApiResponse.ok(returnUsecases.getAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RETAILER', 'SALES', 'ADMIN')")
    public ApiResponse<ReturnResponseDto> update(@PathVariable String id,
            @Valid @RequestBody ReturnUpdateRequestDto request) {
        return ApiResponse.ok(returnUsecases.update(id, request));
    }

    @PutMapping("/status/{id}")
    public ApiResponse<ReturnResponseDto> updateStatus(@PathVariable String id,
            @Valid @RequestBody ReturnStatusUpdateRequestDto request) {
        return ApiResponse.ok(returnUsecases.updateStatus(id, request));
    }

    @PutMapping("/{id}/accept-stock")
    @PreAuthorize("hasAnyRole('WAREHOUSE', 'ADMIN')")
    public ApiResponse<ReturnResponseDto> acceptStock(@PathVariable String id) {
        return ApiResponse.ok(returnUsecases.acceptStock(id));
    }
}
