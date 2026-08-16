package com.inaing.blackhorse_erp.module.returns.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;

public interface IReturnService {

    Return create(Return ret);

    Return getByIdentifier(String identifier);

    List<Return> getAll();

    List<Return> getAllByStatus(ReturnStatus status);

    Return update(Return ret);
}
