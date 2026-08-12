package com.inaing.blackhorse_erp.utils.generators;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

import com.inaing.blackhorse_erp.common.domain.enums.CodeType;

public class CodeGeneratorUtil {

    public static String generateCode(CodeType type) {
        String uniqueInput = type.name() + System.nanoTime() + UUID.randomUUID();
        return generateHashedCode(type, uniqueInput);
    }

    private static String generateHashedCode(CodeType type, String input) {
        try {
            // Use SHA-256 for hashing
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());

            // Convert to URL-safe Base64 and remove non-alphanumeric chars
            String encoded = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(hash)
                    .replaceAll("[^A-Z0-9]", "")
                    .substring(0, type.getLength());

            return type.getPrefix() + "-" + encoded;
        } catch (Exception e) {
            throw new RuntimeException("Code generation failed", e);
        }
    }

}
