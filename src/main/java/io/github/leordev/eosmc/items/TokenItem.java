package io.github.leordev.eosmc.items;

import io.github.leordev.eosmc.utils.StringHelper;
import org.bukkit.Material;

import java.util.List;
import java.util.Optional;

public class TokenItem {
    private int srcItemId;
    private String srcItemName;
    private String eosCategory;
    private String eosTokenName;
    private boolean fungible = true;
    private boolean burnable = true;
    private boolean transferable = true;
    private long maxSupply = 999999999999L;

    private static final String DEFAULT_CATEGORY = "minecraft";

    public TokenItem(Material material) {
        this.srcItemId = material.getId();
        this.srcItemName = material.getKey().toString();
        this.eosCategory = material.getKey().getNamespace();
        this.eosTokenName = TokenHandler.tokenizeItemName(material.getKey().getKey());
    }

    public int getSrcItemId() {
        return srcItemId;
    }

    public String getSrcItemName() {
        return srcItemName;
    }

    public String getEosCategory() {
        return eosCategory;
    }

    public String getEosTokenName() {
        return eosTokenName;
    }

    public boolean isFungible() {
        return fungible;
    }

    public boolean isBurnable() {
        return burnable;
    }

    public boolean isTransferable() {
        return transferable;
    }

    public long getMaxSupply() {
        return maxSupply;
    }

    public void deduplicateName(List<TokenItem> tokens) {
        String name = eosTokenName;

        Optional<TokenItem> duplicatedToken = tokens.stream().filter(token -> token.eosTokenName.equals(eosTokenName)).findAny();
        int i = 1;
        while (duplicatedToken.isPresent()) {
            name = StringHelper.removeLastChars(name, String.valueOf(i).length()) + i;
            final String nextName = name;
            duplicatedToken = tokens.stream().filter(token -> token.eosTokenName.equals(nextName)).findAny();
            i += i % 5 == 0 ? 6 : 1;
        }

        eosTokenName = name;
    }


}
