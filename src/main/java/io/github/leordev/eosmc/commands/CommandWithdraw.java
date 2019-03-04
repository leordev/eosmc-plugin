package io.github.leordev.eosmc.commands;

import io.github.leordev.eosmc.api.ApiWithdraw;
import io.github.leordev.eosmc.gui.GuiWithdraw;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class CommandWithdraw implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        String playerAccount = PlayerMetaData.getEosAccount(player);
        if (playerAccount.isEmpty()) {
            MessageHelper.sendWarning(player, Lang.ACC_HELP);
            return true;
        }

        List<ItemStack> chest;
        try {
            chest = ApiWithdraw.getChest(playerAccount);
            if (chest.size() < 1) {
                MessageHelper.sendWarning(player, Lang.WT_FAIL_EMPTY);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            MessageHelper.sendWarning(player, Lang.WT_FAIL_ITEM, e.getMessage());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            MessageHelper.sendWarning(player, Lang.WT_FAIL);
            return true;
        }

        GuiWithdraw gui = new GuiWithdraw(player, chest);
        player.openInventory(gui.getInventory());

        return true;
    }
}
