package io.github.leordev;

import io.github.leordev.config.EosConfig;
import io.github.leordev.utils.HttpReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER;

public class EosMcPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable(){
        //Fired when the server enables the plugin
        getLogger().info(">>> Enabling EOS Plugin");

        EosConfig.initialize(getConfig());
        saveConfig();

        // Enable our class to check for new players using onPlayerJoin()
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable(){
        //Fired when the server stops and disables all plugins
        getLogger().info(">>> Disabling EOS Plugin");
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String account = EosConfig.getAccount();
        if (account != null && account.length() > 0) {
            player.sendMessage(">>>> Hello from " + account);
        } else {
            player.sendMessage("Oh no, admin didn't setup any EOS Account!");
        }
    }

}
