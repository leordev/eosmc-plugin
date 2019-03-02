package io.github.leordev.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface GuiEos extends InventoryHolder {
    void onGuiClick(InventoryClickEvent event);
    void onGuiClose(Player player);
}