package com.inaing.blackhorse_erp.security.jwt;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "blackhorse.security.jwt")
public record JwtProperties(String secret, Duration accessTokenTtl) {

}
