package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllReturnsUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;

    @Transactional(readOnly = true)
    public List<ReturnResponseDto> execute() {
        return returnService.getAll()
                .stream()
                .map(returnMapper::toResponse)
                .toList();
    }
}
