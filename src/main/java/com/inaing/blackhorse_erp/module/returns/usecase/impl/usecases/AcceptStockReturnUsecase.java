package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.returns.service.IReturnStatusHistoryService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AcceptStockReturnUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final IReturnStatusHistoryService returnStatusHistoryService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public ReturnResponseDto execute(String id) {

        Return ret = returnService.getByIdentifier(id);
        if (ret == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Return not found" + id);
        }

        if (ret.getStatus() != ReturnStatus.APPROVED || ret.getReason() != ReturnReason.STOCK) {
            throw new BusinessRuleException(
                    "RETURN_NOT_ACCEPTABLE",
                    "Only approved stock returns can be accepted.");
        }

        AuthPrincipal actor = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        ret.setStatus(ReturnStatus.COMPLETED);
        returnStatusHistoryService.record(ret, ReturnStatus.COMPLETED, ActionTrigger.MANUAL, actor);

        // add stock to warehouse repository
        return returnMapper.toResponse(returnService.update(ret));
    }
}
