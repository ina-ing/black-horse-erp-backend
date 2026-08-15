package com.inaing.blackhorse_erp.config.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.inaing.blackhorse_erp.module.employee.domain.Employee;
import com.inaing.blackhorse_erp.module.employee.service.IEmployeeService;
import com.inaing.blackhorse_erp.module.role.domain.Role;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AdminSeederProperties.class)
public class AdminSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminSeeder.class);

    private final AdminSeederProperties properties;
    private final IEmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.enabled()) {
            log.info("Admin seeding is disabled, skipping");
            return;
        }

        if (!StringUtils.hasText(properties.phone())) {
            log.warn("Admin seeding skipped: blackhorse.seed.admin.phone is not set");
            return;
        }

        if (!StringUtils.hasText(properties.password())) {
            log.warn("Admin seeding skipped: blackhorse.seed.admin.password is not set");
            return;
        }

        if (employeeService.existsByRole(Role.ADMIN)) {
            log.info("An admin account already exists, skipping seed");
            return;
        }

        if (employeeService.findByPhone(properties.phone()) != null) {
            log.warn("Admin seeding skipped: phone {} is already registered to another employee",
                    properties.phone());
            return;
        }

        Employee admin = Employee.of(
                properties.fullname(),
                passwordEncoder.encode(properties.password()),
                properties.phone(),
                properties.email(),
                null,
                Role.ADMIN,
                null,
                null);

        Employee created = employeeService.create(admin);
        log.info("Seeded admin {} with phone {}", created.getCode(), created.getPhone());
    }
}
