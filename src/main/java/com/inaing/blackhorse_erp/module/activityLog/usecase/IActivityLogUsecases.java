package com.inaing.blackhorse_erp.module.activityLog.usecase;

import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.module.activityLog.dto.request.ActivityLogQueryParams;
import com.inaing.blackhorse_erp.module.activityLog.dto.response.ActivityLogResponseDto;

public interface IActivityLogUsecases {

    PageResponse<ActivityLogResponseDto> getActivityLogs(ActivityLogQueryParams params);
}
