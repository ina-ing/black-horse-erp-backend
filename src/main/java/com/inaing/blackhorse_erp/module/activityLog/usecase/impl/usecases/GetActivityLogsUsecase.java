package com.inaing.blackhorse_erp.module.activityLog.usecase.impl.usecases;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.pagination.DatePeriod;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRangeResolver;
import com.inaing.blackhorse_erp.common.dto.pagination.PageableFactory;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogFilter;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogQueryParams;
import com.inaing.blackhorse_erp.module.activityLog.dto.response.ActivityLogResponseDto;
import com.inaing.blackhorse_erp.module.activityLog.mapper.ActivityLogMapper;
import com.inaing.blackhorse_erp.module.activityLog.service.IActivityLogService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetActivityLogsUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "date", "occurredAt",
            "occurredAt", "occurredAt");

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "occurredAt");

    private final IActivityLogService activityLogService;
    private final ActivityLogMapper activityLogMapper;

    @Transactional(readOnly = true)
    public PageResponse<ActivityLogResponseDto> execute(ActivityLogQueryParams params) {

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        DateRange dateRange = DateRangeResolver.resolve(
                DatePeriod.fromNullable(params.getPeriod()),
                params.getDate(), params.getFrom(), params.getTo());

        ActivityLogFilter filter = new ActivityLogFilter(
                dateRange, params.getPriority(), params.getEntityType(), params.getSearch());

        Page<ActivityLogResponseDto> page = activityLogService.getActivityLogs(filter, pageable)
                .map(activityLogMapper::toResponse);

        return PageResponse.of(page);
    }
}
