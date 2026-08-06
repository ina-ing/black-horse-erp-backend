package com.inaing.blackhorse_erp.module.returns.service;

import java.util.List;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.ReturnStatusHistory;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

public interface IReturnStatusHistoryService {

    void record(Return ret, ReturnStatus status, ActionTrigger trigger, AuthPrincipal actor);

    List<ReturnStatusHistory> getByReturn(String returnId);
}