package io.github.leordev.eosmc.items;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.leordev.eosmc.EosMcPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TokenHandler {

    private final static int MAX_MIDDLE_WORD_LENGTH = 3;
    private final static int TOKEN_NAME_LENGTH = 12;
    public static List<TokenItem> tokens = new ArrayList<>();

    public static List<TokenItem> initializeTokens() {
        tokens = new ArrayList<>();
        for (Material material : Material.values()) {
            if (!material.isItem()) continue;
            TokenItem item = new TokenItem(material);
            item.deduplicateName(tokens);
            tokens.add(item);
        }
        return tokens;
    }

    public static TokenItem fromItem(ItemStack item) {
        String name = item.getData().getItemType().toString();
        Optional<TokenItem> token = tokens.stream()
                .filter(t -> t.getSrcItemName().equals(name))
                .findFirst();
        if (token.isPresent()) {
            return token.get();
        }
        throw new IllegalArgumentException("Invalid item " + name);
    }

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

    public static ItemStack fromTokenJson(JsonObject obj) {
        String name = obj.get("token_name").getAsString();

        Optional<TokenItem> token = tokens.stream()
                .filter(t -> t.getEosTokenName().equals(name))
                .findFirst();
        if (token.isPresent()) {
            Double tokenAmount = obj.get("amount").getAsDouble();
            int amount = tokenAmount > 64 ? 64 : tokenAmount.intValue();
            ItemStack item = new ItemStack(Material.getMaterial(token.get().getSrcItemName()), amount);
            return item;
        }
        throw new IllegalArgumentException("Invalid item " + name);
    }
}
