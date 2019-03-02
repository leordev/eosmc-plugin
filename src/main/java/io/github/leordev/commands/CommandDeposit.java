package io.github.leordev.commands;

import io.github.leordev.gui.GuiDeposit;
import io.github.leordev.player.PlayerMetaData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDeposit implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (!PlayerMetaData.validateAccountAndMessagePlayer(player)) return true;

        GuiDeposit gui = new GuiDeposit();
        player.openInventory(gui.getInventory());

        return true;
    }
}
