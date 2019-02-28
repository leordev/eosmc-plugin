package io.github.leordev;

import io.github.leordev.commands.CommandDump;
import io.github.leordev.config.EosConfig;
import io.github.leordev.player.JoinHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class EosMcPlugin extends JavaPlugin implements Listener {

    public static Logger LOGGER;
    public static File DATA_FOLDER;

    @Override
    public void onEnable(){
        LOGGER = getLogger();

        LOGGER.info(">>> Enabling EOS Plugin");

        DATA_FOLDER = getDataFolder();

        EosConfig.initialize(getConfig());
        saveConfig();

        // Enable our class to check for new players using onPlayerJoin()
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("dump").setExecutor(new CommandDump());
    }

    @Override
    public void onDisable(){
        //Fired when the server stops and disables all plugins
        LOGGER.info(">>> Disabling EOS Plugin");
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        JoinHandler.execute(event);
    }

}
