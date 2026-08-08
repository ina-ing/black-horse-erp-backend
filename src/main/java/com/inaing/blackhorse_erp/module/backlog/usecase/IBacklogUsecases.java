package com.inaing.blackhorse_erp.module.backlog.usecase;

import com.inaing.blackhorse_erp.module.backlog.dto.response.BacklogResponseDto;

public interface IBacklogUsecases {

    BacklogResponseDto getByFactory(String factoryIdentifier);
}
