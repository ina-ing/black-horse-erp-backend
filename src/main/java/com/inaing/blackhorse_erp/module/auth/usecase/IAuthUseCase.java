package com.inaing.blackhorse_erp.module.auth.usecase;

import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;

public interface IAuthUseCase<RES, REQ> {
    LoginResult<REQ> login(RES request);
}
