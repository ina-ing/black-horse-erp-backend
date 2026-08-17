package com.inaing.blackhorse_erp.module.activityLog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.inaing.blackhorse_erp.module.activityLog.domain.ActivityLog;
import com.inaing.blackhorse_erp.module.activityLog.dto.response.ActivityLogResponseDto;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {

    @Mapping(target = "action", source = "action.label")
    ActivityLogResponseDto toResponse(ActivityLog activityLog);
}
