package com.inaing.blackhorse_erp.module.auth.usecase;

import com.inaing.blackhorse_erp.module.auth.dto.LoginRequestDto;
import com.inaing.blackhorse_erp.module.auth.dto.LoginResult;
import com.inaing.blackhorse_erp.module.auth.dto.LoginUserDto;

public interface IAuthUseCase {

    LoginResult<LoginUserDto> login(LoginRequestDto request);
}
