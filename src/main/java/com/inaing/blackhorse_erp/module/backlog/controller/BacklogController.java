package com.inaing.blackhorse_erp.module.backlog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.module.backlog.dto.response.BacklogResponseDto;
import com.inaing.blackhorse_erp.module.backlog.usecase.IBacklogUsecases;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/production-backlog")
@RequiredArgsConstructor
public class BacklogController {

    private final IBacklogUsecases backlogUsecases;

    @GetMapping("/factory/{identifier}")
    public ApiResponse<BacklogResponseDto> getByFactory(@PathVariable String identifier) {
        return ApiResponse.ok(backlogUsecases.getByFactory(identifier));
    }
}
