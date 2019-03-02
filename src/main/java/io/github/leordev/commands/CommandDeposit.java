package io.github.leordev.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.leordev.EosMcPlugin;
import io.github.leordev.config.EosConfig;
import io.github.leordev.gui.GuiDeposit;
import io.github.leordev.player.PlayerMetaData;
import io.github.leordev.utils.HttpHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

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
