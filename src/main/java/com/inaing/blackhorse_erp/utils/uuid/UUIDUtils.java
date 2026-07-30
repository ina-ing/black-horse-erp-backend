package com.inaing.blackhorse_erp.utils.uuid;

import java.util.UUID;

public class UUIDUtils {

    public static boolean isUUID(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return false;
        }

        try {
            UUID.fromString(identifier);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}