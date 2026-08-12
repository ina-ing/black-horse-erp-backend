package com.inaing.blackhorse_erp.module.warehouse.authorization;

import org.springframework.stereotype.Component;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.module.warehouse.domain.Warehouse;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WarehouseAuthorizationService {

    private final CurrentUserProvider currentUserProvider;

    public void assertCanAccess(Warehouse warehouse) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        if (Role.ADMIN.toString().equals(principal.role())) {
            return;
        }
        if (Role.WAREHOUSE.toString().equals(principal.role())
                && warehouse.getManager().getId().equals(principal.id())) {
            return;
        }

        throw new AppException(ErrorCode.ACCESS_DENIED);
    }
}
