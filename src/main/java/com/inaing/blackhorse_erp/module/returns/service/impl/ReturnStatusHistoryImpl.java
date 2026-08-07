package com.inaing.blackhorse_erp.module.returns.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnStatusHistory;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.repository.ReturnStatusHistoryRepository;
import com.inaing.blackhorse_erp.module.returns.service.IReturnStatusHistoryService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReturnStatusHistoryImpl implements IReturnStatusHistoryService {

    private final ReturnStatusHistoryRepository returnStatusHistoryRepository;

    @Override
    @Transactional
    public void record(Return ret, ReturnStatus status, ActionTrigger trigger, AuthPrincipal principal) {

        ReturnStatusHistory history = ReturnStatusHistory.builder()
                .returnId(ret)
                .status(status)
                .trigger(trigger)
                .principalName(principal.name())
                .principalRole(principal.role())
                .build();

        returnStatusHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReturnStatusHistory> getByReturn(String returnId) {
        return returnStatusHistoryRepository.findByReturnIdIdOrderByCreatedAtAsc(returnId);
    }
}
