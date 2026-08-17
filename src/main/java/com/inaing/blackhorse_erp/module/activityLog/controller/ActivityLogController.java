package com.inaing.blackhorse_erp.module.activityLog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inaing.blackhorse_erp.common.dto.ApiResponse;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogQueryParams;
import com.inaing.blackhorse_erp.module.activityLog.dto.response.ActivityLogResponseDto;
import com.inaing.blackhorse_erp.module.activityLog.usecase.IActivityLogUsecases;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/activity-log")
@RequiredArgsConstructor
public class ActivityLogController {

    private final IActivityLogUsecases activityLogUsecases;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ApiResponse<PageResponse<ActivityLogResponseDto>> getActivityLogs(
            @ModelAttribute ActivityLogQueryParams params) {
        return ApiResponse.ok(activityLogUsecases.getActivityLogs(params));
    }
}
