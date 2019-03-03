package io.github.leordev.eosmc.gui;

import org.bukkit.event.inventory.InventoryAction;

public class GuiHelper {
    public static boolean isDropping(InventoryAction action) {
        return action == InventoryAction.DROP_ALL_CURSOR
                || action == InventoryAction.DROP_ALL_SLOT
                || action == InventoryAction.DROP_ONE_CURSOR
                || action == InventoryAction.DROP_ONE_SLOT;
    }
}
