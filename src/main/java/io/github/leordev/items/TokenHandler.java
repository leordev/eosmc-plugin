package io.github.leordev.items;

public class TokenHandler {

    // TODO: remove final periods and apply reduced separation rules from dumper
    public static String tokenizeItemName(String name) {
        String sanitized = name.toLowerCase().replaceAll("_", ".");
        return sanitized.length() > 12 ?
            sanitized.substring(0, 12) : sanitized;
    }

}
