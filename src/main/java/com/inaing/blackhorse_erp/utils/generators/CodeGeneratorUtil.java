package com.inaing.blackhorse_erp.utils.generators;

import java.security.SecureRandom;

public class CodeGeneratorUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate(String prefix, int length) {
        StringBuilder sb = new StringBuilder(prefix);

        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }

}
