package com.inaing.blackhorse_erp.module.returns.service;

import java.util.List;

import com.inaing.blackhorse_erp.module.returns.domain.Return;

public interface IReturnService {

    Return create(Return ret);

    Return getByIdentifier(String identifier);

    List<Return> getAll();

    Return update(Return ret);

    String generateReturnCode();
}
