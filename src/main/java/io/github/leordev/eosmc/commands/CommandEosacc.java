package io.github.leordev.eosmc.commands;

import io.github.leordev.eosmc.EosMcPlugin;
import io.github.leordev.eosmc.api.ApiAccount;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandEosacc implements CommandExecutor {

    Player player;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        if (args.length != 1) return false;

        player = (Player) sender;
        String account = args[0];

        try {
            attemptToSetAccount(account);
        } catch (IOException e) {
            handleSetAccountError(e);
        }

        return true;
    }

    private void attemptToSetAccount(String account) throws IOException {
        String mcUsername = player.getName();
        MessageHelper.sendInfoAndWait(player, Lang.ACC_SETTING);
        boolean success = ApiAccount.setAccount(player, account);
        if (success) {
            PlayerMetaData.setEosAccount(player, account);
            MessageHelper.sendSuccess(player, Lang.ACC_SET);
            EosMcPlugin.LOGGER.info("Player " + mcUsername + " set account " + account);
        } else {
            MessageHelper.sendError(player, Lang.ACC_SET_FAIL);
        }
    }

    private void handleSetAccountError(IOException e) {
        e.printStackTrace();
        String msg = "Fail to set EOS Account >> \n" + e.getMessage();
        EosMcPlugin.LOGGER.severe(msg);
        MessageHelper.sendError(player, Lang.ACC_SET_FAIL);
    }
}
