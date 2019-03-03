package io.github.leordev.eosmc;

import io.github.leordev.eosmc.commands.CommandDeposit;
import io.github.leordev.eosmc.commands.CommandDump;
import io.github.leordev.eosmc.commands.CommandEosacc;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.gui.GuiEventHandler;
import io.github.leordev.eosmc.items.TokenHandler;
import io.github.leordev.eosmc.player.JoinEventHandler;
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

        TokenHandler.initializeTokens();

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
