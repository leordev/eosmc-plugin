package io.github.leordev.eosmc.gui;

import org.bukkit.event.inventory.InventoryAction;

public class GuiHelper {
    public static boolean isDropping(InventoryAction action) {
        return action == InventoryAction.DROP_ALL_CURSOR
                || action == InventoryAction.DROP_ALL_SLOT
                || action == InventoryAction.DROP_ONE_CURSOR
                || action == InventoryAction.DROP_ONE_SLOT;
    }

    public static boolean isPicking(InventoryAction action) {
        return action == InventoryAction.PICKUP_ALL
                || action == InventoryAction.PICKUP_HALF
                || action == InventoryAction.PICKUP_ONE
                || action == InventoryAction.PICKUP_SOME;
    }

    public static boolean isPlacing(InventoryAction action) {
        return action == InventoryAction.PLACE_ALL
                || action == InventoryAction.PLACE_ONE
                || action == InventoryAction.PLACE_SOME;
    }
}
