package io.github.leordev.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiEventHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof GuiEos) {
//            e.setCancelled(true);
            GuiEos gui = (GuiEos) e.getInventory().getHolder();
            gui.onGuiClick(e);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof GuiEos) {
            GuiEos gui = (GuiEos) e.getInventory().getHolder();
            gui.onGuiClose((Player) e.getPlayer());
        }
    }
}
