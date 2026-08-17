package com.inaing.blackhorse_erp.module.returns.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.projections.ReturnStatusCountProjection;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnFilter;

public interface IReturnService {

    Return create(Return ret);

    Return getByIdentifier(String identifier);

    List<Return> getAll();

    List<Return> getAllByStatus(ReturnStatus status);

    Page<Return> getReturns(ReturnFilter filter, Pageable pageable);

    List<ReturnStatusCountProjection> getStatusCounts(String handledById, String retailerId,
            List<ReturnReason> reasons);

    Return update(Return ret);
}
