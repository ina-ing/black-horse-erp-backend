package com.inaing.blackhorse_erp.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.retailer.domain.Retailer;
import com.inaing.blackhorse_erp.module.retailer.service.IRetailerService;
import com.inaing.blackhorse_erp.security.context.AuthPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    public static final String FAILURE_ATTRIBUTE = "jwt.failure";

    private final JwtService jwtService;

    private final IEmployeeService employeeService;

    private final IRetailerService retailerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null) {
            try {
                authenticate(jwtService.parse(token));
            } catch (ExpiredJwtException ex) {
                reject(request, ErrorCode.TOKEN_EXPIRED);
            } catch (JwtException | IllegalArgumentException ex) {
                reject(request, ErrorCode.TOKEN_INVALID);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void reject(HttpServletRequest request, ErrorCode code) {
        SecurityContextHolder.clearContext();
        request.setAttribute(FAILURE_ATTRIBUTE, code);
    }

    private String resolveToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (AccessTokenCookie.NAME.equals(cookie.getName()) && StringUtils.hasText(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void authenticate(Claims claims) {

        String uid = claims.get(JwtService.CLAIM_UID, String.class);

        Employee employee = employeeService.getByIdentifier(uid);
        Retailer retailer = retailerService.getByIdentifier(uid);

        if (employee == null && retailer == null) {
            throw new JwtException("Invalid claims provided in JWT");
        }

        String role = claims.get(JwtService.CLAIM_ROLE, String.class);
        String name = claims.get(JwtService.CLAIM_NAME, String.class);

        List<SimpleGrantedAuthority> authorities = StringUtils.hasText(role)
                ? List.of(new SimpleGrantedAuthority("ROLE_" + role))
                : List.of();
        var authentication = new UsernamePasswordAuthenticationToken(
                "%s-%s".formatted(name, uid), null, authorities);
        authentication.setDetails(new AuthPrincipal(uid, name, role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
