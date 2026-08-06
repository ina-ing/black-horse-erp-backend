package com.inaing.blackhorse_erp.module.returns.usecase.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnCreationRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.returns.usecase.IReturnUsecases;
import com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases.AcceptStockReturnUsecase;
import com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases.CreateReturnUsecase;
import com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases.GetAllReturnsUsecase;
import com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases.GetReturnUsecase;
import com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases.UpdateReturnStatusUsecase;
import com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases.UpdateReturnUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReturnUsecasesImpl implements IReturnUsecases {

    private final CreateReturnUsecase createReturnUsecase;
    private final GetReturnUsecase getReturnUsecase;
    private final GetAllReturnsUsecase getAllReturnsUsecase;
    private final UpdateReturnUsecase updateReturnUsecase;
    private final UpdateReturnStatusUsecase updateReturnStatusUsecase;
    private final AcceptStockReturnUsecase acceptStockReturnUsecase;

    @Override
    public ReturnResponseDto create(ReturnCreationRequestDto request) {
        return createReturnUsecase.execute(request);
    }

    @Override
    public ReturnWithStatusHistoryResponseDto getByIdentifier(String code) {
        return getReturnUsecase.execute(code);
    }

    @Override
    public List<ReturnResponseDto> getAll() {
        return getAllReturnsUsecase.execute();
    }

    @Override
    public ReturnResponseDto update(String id, ReturnUpdateRequestDto request) {
        return updateReturnUsecase.execute(id, request);
    }

    @Override
    public ReturnResponseDto updateStatus(String id, ReturnStatusUpdateRequestDto request) {
        return updateReturnStatusUsecase.execute(id, request);
    }

    @Override
    public ReturnResponseDto acceptStock(String id) {
        return acceptStockReturnUsecase.execute(id);
    }
}
