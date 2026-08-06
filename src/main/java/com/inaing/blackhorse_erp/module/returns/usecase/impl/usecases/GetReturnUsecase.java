package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnStatusHistory;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnWithStatusHistoryResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.returns.service.IReturnStatusHistoryService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetReturnUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final IReturnStatusHistoryService returnStatusHistoryService;

    @Transactional(readOnly = true)
    public ReturnWithStatusHistoryResponseDto execute(String code) {
        Return ret = returnService.getByIdentifier(code);

        if (ret == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Return not found" + code);
        }

        List<ReturnStatusHistory> histories = returnStatusHistoryService.getByReturn(ret.getId());
        List<ReturnStatusHistoryResponseDto> historyResponse = returnMapper.toHistoryResponseList(histories);

        return returnMapper.toResponseWithHistory(ret, historyResponse);
    }
}
