package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.domain.enums.ActionTrigger;
import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.exception.exceptions.BusinessRuleException;
import com.inaing.blackhorse_erp.module.returns.domain.Return;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnStatusUpdateRequestDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnResponseDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.returns.service.IReturnStatusHistoryService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateReturnStatusUsecase {

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final IReturnStatusHistoryService returnStatusHistoryService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public ReturnResponseDto execute(String id, ReturnStatusUpdateRequestDto request) {

        Return ret = returnService.getByIdentifier(id);
        if (ret == null) {
            throw new AppException(ErrorCode.NOT_FOUND, "Return not found" + id);
        }

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Role role = Role.fromName(principal.role());

        switch (request.status()) {
            case APPROVED -> approve(ret, role, principal);
            case CANCELLED -> cancel(ret, role, principal);
            case PROCESSING -> startProcessing(ret, role, principal);
            case DISPATCHED -> dispatch(ret, role, principal);
            case COMPLETED -> complete(ret, role, principal);
            default -> throw new BusinessRuleException(
                    "INVALID_STATUS_TRANSITION",
                    "This status cannot be set manually.");
        }
        return returnMapper.toResponse(returnService.update(ret));
    }

    private void approve(Return ret, Role role, AuthPrincipal principal) {
        if (role != Role.ADMIN) {
            if (role != Role.SALES
                    || ret.getHandledBy() == null
                    || !principal.id().equals(ret.getHandledBy().getId())) {
                throw new AppException(ErrorCode.RETURN_UPDATE_DENIED);
            }
        }
        if (ret.getStatus() != ReturnStatus.PENDING) {
            throw new BusinessRuleException(
                    "RETURN_NOT_APPROVABLE",
                    "Only pending returns can be approved.");
        }
        boolean hasActiveItems = ret.getItems()
                .stream()
                .anyMatch(item -> item.getQuantity() > 0);

        if (!hasActiveItems) {
            throw new BusinessRuleException(
                    "EMPTY_RETURN",
                    "Cannot approve an return with no active items.");
        }

        ret.setStatus(ReturnStatus.APPROVED);
        returnStatusHistoryService.record(ret, ReturnStatus.APPROVED, ActionTrigger.MANUAL, principal);
    }

    private void startProcessing(Return ret, Role role, AuthPrincipal principal) {

        if (role != Role.FACTORY) {
            throw new BusinessRuleException(
                    "RETURN_ACCEPT_DENIED",
                    "Only factory users can accept return.");
        }
        if (ret.getStatus() != ReturnStatus.APPROVED) {
            throw new BusinessRuleException(
                    "RETURN_NOT_ACCEPTABLE",
                    "Only approved returns can be accepted by the factory.");
        }

        ret.setStatus(ReturnStatus.PROCESSING);
        returnStatusHistoryService.record(ret, ReturnStatus.PROCESSING, ActionTrigger.MANUAL, principal);
    }

    private void dispatch(Return ret, Role role, AuthPrincipal principal) {
        if (role != Role.FACTORY) {
            throw new BusinessRuleException(
                    "RETURN_DISPATCH_DENIED",
                    "Only factory users can dispatch return.");
        }
        if (ret.getStatus() != ReturnStatus.PROCESSING) {
            throw new BusinessRuleException(
                    "RETURN_NOT_DISPATCHABLE",
                    "Only processing returns can be dispatched by the factory.");
        }

        ret.setStatus(ReturnStatus.DISPATCHED);
        returnStatusHistoryService.record(ret, ReturnStatus.DISPATCHED, ActionTrigger.MANUAL, principal);
    }

    private void complete(Return ret, Role role, AuthPrincipal principal) {

        if (role != Role.RETAILER
                || !ret.getRetailer().getId().equals(principal.id())) {
            throw new BusinessRuleException(
                    "RETURN_NOT_ACCEPTABLE",
                    "You are not allowed to accept this return.");
        }

        ret.setStatus(ReturnStatus.COMPLETED);
        returnStatusHistoryService.record(ret, ReturnStatus.COMPLETED, ActionTrigger.FULFILLMENT, principal);
    }

    private void cancel(Return ret, Role role, AuthPrincipal principal) {

        if (role != Role.ADMIN) {
            switch (role) {
                case RETAILER -> {
                    if (!ret.getRetailer().getId().equals(principal.id())) {
                        throw new BusinessRuleException(
                                "RETURN_CANCEL_DENIED",
                                "You can only cancel your own returns.");
                    }
                }
                case SALES -> {
                    if (ret.getHandledBy().getId().equals(principal.id())) {
                        throw new BusinessRuleException(
                                "RETURN_CANCEL_DENIED",
                                "You not allowed to cancel this return");
                    }
                }
                default -> throw new BusinessRuleException(
                        "ORDER_CANCEL_DENIED",
                        "You are not allowed to cancel returns.");
            }
        }

        if (ret.getStatus() != ReturnStatus.PENDING && ret.getStatus() != ReturnStatus.APPROVED) {
            throw new BusinessRuleException(
                    "RETURN_NOT_CANCELLABLE",
                    "Only pending or approved returns can be cancelled");
        }

        ret.setStatus(ReturnStatus.CANCELLED);
        returnStatusHistoryService.record(ret, ReturnStatus.CANCELLED, ActionTrigger.CANCELLATION, principal);
    }
}
