package com.inaing.blackhorse_erp.config;


import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.inaing.blackhorse_erp.security.context.CurrentUserProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class JpaAuditingConfig implements AuditorAware<String> {

    private static final String SYSTEM = "system";
  
    private final CurrentUserProvider currentUserProvider;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(currentUserProvider.currentUsername().orElse(SYSTEM));
    }
}
