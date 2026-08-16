package com.inaing.blackhorse_erp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "blackhorse.s3")
public record S3Properties(
        String region,
        String endpoint,
        String accessKey,
        String secretKey,
        String bucketName,
        String publicUrl) {
}
