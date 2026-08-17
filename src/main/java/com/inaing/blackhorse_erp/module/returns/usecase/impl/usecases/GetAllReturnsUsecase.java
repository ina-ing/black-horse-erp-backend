package com.inaing.blackhorse_erp.module.returns.usecase.impl.usecases;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.common.dto.list.PagedListWithAnalytics;
import com.inaing.blackhorse_erp.common.dto.pagination.DatePeriod;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRange;
import com.inaing.blackhorse_erp.common.dto.pagination.DateRangeResolver;
import com.inaing.blackhorse_erp.common.dto.pagination.PageableFactory;
import com.inaing.blackhorse_erp.common.dto.response.PageResponse;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnReason;
import com.inaing.blackhorse_erp.module.returns.domain.enums.ReturnStatus;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnFilter;
import com.inaing.blackhorse_erp.module.returns.dto.request.ReturnQueryParams;
import com.inaing.blackhorse_erp.module.returns.dto.response.ReturnListResponseDto;
import com.inaing.blackhorse_erp.module.returns.dto.response.analytics.ReturnBasicAnalyticsDto;
import com.inaing.blackhorse_erp.module.returns.mapper.ReturnMapper;
import com.inaing.blackhorse_erp.module.returns.service.IReturnService;
import com.inaing.blackhorse_erp.module.role.domain.Role;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;
import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GetAllReturnsUsecase {

    private static final Map<String, String> SORT_ALLOWLIST = Map.of(
            "returnDate", "returnDate",
            "totalQuantity", "totalQuantity",
            "totalArticles", "totalArticles");

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "returnDate");

    private static final List<ReturnReason> ALL_REASONS = Arrays.asList(ReturnReason.values());

    private final ReturnMapper returnMapper;
    private final IReturnService returnService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public PagedListWithAnalytics<ReturnBasicAnalyticsDto, ReturnListResponseDto> execute(
            ReturnQueryParams params) {

        AuthPrincipal principal = currentUserProvider.currentPrincipal()
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        Role role = Role.fromName(principal.role());
        String handledById = role == Role.SALES ? principal.id() : null;
        String retailerId = role == Role.RETAILER ? principal.id() : null;
        List<ReturnReason> scopedReasons = reasonsFor(role);

        Pageable pageable = PageableFactory.from(params, SORT_ALLOWLIST, DEFAULT_SORT);

        DateRange dateRange = DateRangeResolver.resolve(
                DatePeriod.fromNullable(params.getPeriod()),
                params.getDate(), params.getFrom(), params.getTo());

        ReturnFilter filter = new ReturnFilter(
                dateRange, params.getStatuses(), reasons(scopedReasons, params.getReason()),
                params.getSearch(), handledById, retailerId);

        Page<ReturnListResponseDto> page = returnService.getReturns(filter, pageable)
                .map(returnMapper::toListResponse);

        return PagedListWithAnalytics.of(
                analytics(handledById, retailerId, scopedReasons), PageResponse.of(page));
    }

    // Warehouse handles stock returns, the factory handles the ones it has to work on;
    // every other role sees them all.
    private List<ReturnReason> reasonsFor(Role role) {
        return switch (role) {
            case WAREHOUSE -> List.of(ReturnReason.STOCK);
            case FACTORY -> List.of(ReturnReason.DAMAGE, ReturnReason.REPAIR);
            default -> ALL_REASONS;
        };
    }

    // A requested reason narrows the role's scope; asking for one outside it matches
    // nothing rather than quietly widening the scope.
    private List<ReturnReason> reasons(List<ReturnReason> scoped, ReturnReason requested) {
        if (requested == null) {
            return scoped;
        }
        return scoped.contains(requested) ? List.of(requested) : List.of();
    }

    private ReturnBasicAnalyticsDto analytics(String handledById, String retailerId,
            List<ReturnReason> scopedReasons) {

        Map<ReturnStatus, Long> byStatus = new EnumMap<>(ReturnStatus.class);
        for (ReturnStatus status : ReturnStatus.values()) {
            byStatus.put(status, 0L);
        }
        returnService.getStatusCounts(handledById, retailerId, scopedReasons)
                .forEach(count -> byStatus.put(count.getStatus(), count.getCount()));

        long total = byStatus.values().stream().mapToLong(Long::longValue).sum();

        return new ReturnBasicAnalyticsDto(total, byStatus);
    }
}
