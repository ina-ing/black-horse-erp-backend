package com.inaing.blackhorse_erp.module.activityLog.usecase.impl;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogQueryParams;
import com.inaing.blackhorse_erp.module.activityLog.dto.response.ActivityLogResponseDto;
import com.inaing.blackhorse_erp.module.activityLog.usecase.IActivityLogUsecases;
import com.inaing.blackhorse_erp.module.activityLog.usecase.impl.usecases.GetActivityLogsUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ActivityLogUsecasesImpl implements IActivityLogUsecases {

    private final GetActivityLogsUsecase getActivityLogsUsecase;

    @Override
    public PageResponse<ActivityLogResponseDto> getActivityLogs(ActivityLogQueryParams params) {
        return getActivityLogsUsecase.execute(params);
    }
}
