package io.github.leordev.eosmc.utils;

public class StringHelper {
    public static String removeLastChars(String str, int charsToRemove) {
        return str.substring(0, str.length() - charsToRemove);
    }
}
