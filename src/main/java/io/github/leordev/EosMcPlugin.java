package io.github.leordev;

import io.github.leordev.commands.CommandDeposit;
import io.github.leordev.commands.CommandDump;
import io.github.leordev.commands.CommandEosacc;
import io.github.leordev.config.EosConfig;
import io.github.leordev.gui.GuiEventHandler;
import io.github.leordev.player.JoinEventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class EosMcPlugin extends JavaPlugin implements Listener {

    public static Logger LOGGER;
    public static File DATA_FOLDER;
    public static EosMcPlugin instance;

    @Override
    public void onEnable(){
        instance = this;
        LOGGER = getLogger();

        LOGGER.info(">>> Enabling EOS Plugin");

        DATA_FOLDER = getDataFolder();

        EosConfig.initialize(getConfig());
        saveConfig();

        // Enable our class to check for new players using onPlayerJoin()
        getServer().getPluginManager().registerEvents(new JoinEventHandler(), this);
        getServer().getPluginManager().registerEvents(new GuiEventHandler(), this);
        getCommand("dump").setExecutor(new CommandDump());
        getCommand("eosacc").setExecutor(new CommandEosacc());
        getCommand("deposit").setExecutor(new CommandDeposit());
    }

    @Override
    public void onDisable(){
        //Fired when the server stops and disables all plugins
        LOGGER.info(">>> Disabling EOS Plugin");
    }

}
