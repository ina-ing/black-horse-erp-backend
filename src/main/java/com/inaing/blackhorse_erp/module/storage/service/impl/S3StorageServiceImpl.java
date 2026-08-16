package com.inaing.blackhorse_erp.module.storage.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.inaing.blackhorse_erp.common.dto.ErrorCode;
import com.inaing.blackhorse_erp.config.S3Properties;
import com.inaing.blackhorse_erp.exception.exceptions.AppException;
import com.inaing.blackhorse_erp.module.storage.service.IStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageServiceImpl implements IStorageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png", "image/webp", "image/avif");

    private final S3Client s3Client;
    private final S3Properties properties;

    @Override
    public String upload(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new AppException(ErrorCode.VALIDATION_FAILED, "File is required");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.VALIDATION_FAILED, "File must be 5MB or smaller");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new AppException(ErrorCode.VALIDATION_FAILED,
                    "Unsupported file type. Allowed: " + String.join(", ", ALLOWED_CONTENT_TYPES));
        }

        String key = buildKey(folder, file.getOriginalFilename());

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(properties.bucketName())
                            .key(key)
                            .contentType(contentType)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Could not read the uploaded file");
        } catch (S3Exception e) {
            log.error(e.getMessage(), e);
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Could not upload the file to storage");
        }

        return buildPublicUrl(key);
    }

    @Override
    public void deleteByUrl(String url) {
        String key = extractKey(url);
        if (key == null) {
            return;
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(properties.bucketName())
                    .key(key)
                    .build());
        } catch (S3Exception e) {
            log.warn("Could not delete {} from storage", key, e);
        }
    }

    private String extractKey(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }

        for (String base : List.of(
                StringUtils.hasText(properties.publicUrl()) ? trimTrailingSlash(properties.publicUrl()) : "",
                StringUtils.hasText(properties.endpoint())
                        ? trimTrailingSlash(properties.endpoint()) + "/" + properties.bucketName()
                        : "",
                "https://" + properties.bucketName() + ".s3." + properties.region() + ".amazonaws.com")) {

            if (StringUtils.hasText(base) && url.startsWith(base + "/")) {
                return url.substring(base.length() + 1);
            }
        }

        log.warn("Skipping delete, {} is not a managed storage url", url);
        return null;
    }

    private String buildKey(String folder, String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String name = UUID.randomUUID().toString();
        String prefix = StringUtils.hasText(folder) ? folder + "/" : "";

        return StringUtils.hasText(extension)
                ? prefix + name + "." + extension.toLowerCase(Locale.ROOT)
                : prefix + name;
    }

    private String buildPublicUrl(String key) {
        if (StringUtils.hasText(properties.publicUrl())) {
            return trimTrailingSlash(properties.publicUrl()) + "/" + key;
        }
        if (StringUtils.hasText(properties.endpoint())) {
            return trimTrailingSlash(properties.endpoint()) + "/" + properties.bucketName() + "/" + key;
        }
        return "https://" + properties.bucketName() + ".s3." + properties.region() + ".amazonaws.com/" + key;
    }

    private String trimTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
