package io.github.leordev.eosmc.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.leordev.eosmc.EosMcPlugin;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.HttpHandler;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        MessageHelper.sendInfo(player, Lang.ACC_SETTING, true);

        try {
            String url = EosConfig.getInterfaceServer() + "/player/" + account + "/confirm";
            String response = HttpHandler.postUrl(url, data);
            JsonElement json = new JsonParser().parse(response);
            boolean success = json.getAsJsonObject().get("success").getAsBoolean();
            if (success) {
                PlayerMetaData.setEosAccount(player, account);
                MessageHelper.sendSuccess(player, Lang.ACC_SET);
                EosMcPlugin.LOGGER.info("Player " + mcUsername + " set account " + account);
            } else {
                MessageHelper.sendError(player, Lang.ACC_SET_FAIL);
                EosMcPlugin.LOGGER.severe(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            String msg = "Fail to set EOS Account >> \n" + e.getMessage();
            EosMcPlugin.LOGGER.severe(msg);
            MessageHelper.sendError(player, Lang.ACC_SET_FAIL);
            return false;
        }

        return true;
    }
}
