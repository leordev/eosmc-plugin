package io.github.leordev.eosmc.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TokenHandler {

    private final static int MAX_MIDDLE_WORD_LENGTH = 3;
    private final static int TOKEN_NAME_LENGTH = 12;

    public static String tokenizeItemName(String name) {
        String alphabetized = name.replaceAll("0|[6-9]", "1");
        String sanitized = alphabetized.toLowerCase().replaceAll("_", ".");
        String reduced = reduce(sanitized);
        return unPeriod(reduced);
    }

    private static String reduce(String name) {
        String[] words = name.split("\\.");
        List<String> result = new ArrayList<>();

        int preLastWordIndex = words.length - 1;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i > 0) {
                boolean isNotLast = i < preLastWordIndex;
                boolean isBigWord = word.length() > MAX_MIDDLE_WORD_LENGTH;
                int endPosition = isNotLast && isBigWord
                        ? MAX_MIDDLE_WORD_LENGTH
                        : word.length();
                result.add(word.substring(0, endPosition));
            } else {
                result.add(word);
            }
        }

        return String.join(".", result);
    }

    private static String unPeriod(String name) {
        int end = name.length() > TOKEN_NAME_LENGTH
                ? TOKEN_NAME_LENGTH
                : name.length();

        String unPeriodRes = name;
        int lastPosition = TOKEN_NAME_LENGTH-1;
        int nextPosition = TOKEN_NAME_LENGTH;

        if (end == TOKEN_NAME_LENGTH
                && name.charAt(lastPosition) == '.') {
            String adjustedName = unPeriodRes;
            while(adjustedName.charAt(lastPosition) == '.') {
                adjustedName = adjustedName.length() > TOKEN_NAME_LENGTH
                        ? adjustedName.substring(0, lastPosition) + adjustedName.charAt(nextPosition)
                        : adjustedName.substring(0, lastPosition) + "1";
            }
            unPeriodRes = adjustedName;
        }

        return unPeriodRes.substring(0, end);
    }

    public static List<TokenItem> getTokensList() {
        List<TokenItem> tokens = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isItem()) continue;
            TokenItem item = new TokenItem(material.getId(), material.name());
            item.eosTokenName = deduplicateName(tokens, item.eosTokenName);
            tokens.add(item);
        }
        return tokens;
    }

    private static String deduplicateName(List<TokenItem> tokens, String eosTokenName) {
        String name = eosTokenName;

        Optional<TokenItem> duplicatedToken = tokens.stream().filter(token -> token.eosTokenName.equals(eosTokenName)).findAny();
        int i = 1;
        while (duplicatedToken.isPresent()) {
            name = removeLastChars(name, String.valueOf(i).length()) + i;
            final String nextName = name;
            duplicatedToken = tokens.stream().filter(token -> token.eosTokenName.equals(nextName)).findAny();
            i += i % 5 == 0 ? 6 : 1;
        }

        return name;
    }

    private static String removeLastChars(String str, int charsToRemove) {
        return str.substring(0, str.length() - charsToRemove);
    }

}
