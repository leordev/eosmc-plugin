package io.github.leordev.eosmc.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface GuiEos extends InventoryHolder {
    void onGuiClick(InventoryClickEvent event);
    void onGuiClose();
}