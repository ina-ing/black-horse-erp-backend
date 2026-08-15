package com.inaing.blackhorse_erp.config.seeder;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blackhorse.seed.admin")
public record AdminSeederProperties(
        boolean enabled,
        String fullname,
        String phone,
        String password,
        String email) {

}
