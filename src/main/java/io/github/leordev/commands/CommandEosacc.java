package io.github.leordev.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.leordev.EosMcPlugin;
import io.github.leordev.config.EosConfig;
import io.github.leordev.player.PlayerMetaData;
import io.github.leordev.utils.HttpHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.IOException;

public class CommandEosacc implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        if (args.length != 1) return false;

        Player player = (Player) sender;
        String account = args[0];
        String mcUsername = player.getName();

        String data = "{\"mcUsername\": \"" + mcUsername + "\"}";
        EosMcPlugin.LOGGER.info("Setting player: " + data);
        player.sendMessage("Setting EOS account, please wait...");

        try {
            String url = EosConfig.getInterfaceServer() + "/player/" + account + "/confirm";
            String response = HttpHandler.postUrl(url, data);
            JsonElement json = new JsonParser().parse(response);
            boolean success = json.getAsJsonObject().get("success").getAsBoolean();
            if (success) {
                PlayerMetaData.setEosAccount(player, account);
                player.sendMessage("EOS account was set properly!");
                EosMcPlugin.LOGGER.info("Player " + mcUsername + " set account " + account);
            } else {
                player.sendMessage("Fail to set EOS Account");
                EosMcPlugin.LOGGER.severe(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            String msg = "Fail to set EOS Account >> \n" + e.getMessage();
            EosMcPlugin.LOGGER.severe(msg);
            player.sendMessage(msg);
            return false;
        }

        return true;
    }
}
