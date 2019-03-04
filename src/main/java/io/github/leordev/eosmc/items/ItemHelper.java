package io.github.leordev.eosmc.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemHelper {

    public static ItemStack emptyItem () {
        return new ItemStack(Material.AIR);
    }

    public static boolean isEmpty(ItemStack item) {
        return item == null
                || item.getType().equals(Material.AIR)
                || item.getAmount() == 0;
    }
}
