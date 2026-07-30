package com.inaing.blackhorse_erp.utils;

public class StringUtils {
    public static String generateIdentifier(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }

        return title.toLowerCase()
                .trim()
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
