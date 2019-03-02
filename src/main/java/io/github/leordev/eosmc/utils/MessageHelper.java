package io.github.leordev.eosmc.utils;

import io.github.leordev.eosmc.i18n.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHelper {
    public static void send(Player player, String message) {
        player.sendMessage(message);
    }

    public static void sendError(Player player, Lang message) {
        send(player, ChatColor.RED + message.toString());
    }

    public static void sendSuccess(Player player, Lang message) {
        send(player, ChatColor.GREEN + message.toString());
    }

    public static void sendSuccess(Player player, Lang message, String... args) {
        send(player, ChatColor.GREEN + message.toString() + args[0]); // TODO: parse args
    }

    public static void sendWarning(Player player, Lang message) {
        send(player, ChatColor.GOLD + message.toString());
    }

    public static void sendInfo(Player player, Lang message, boolean andSendWait) {
        send(player, ChatColor.AQUA + message.toString());
        if (andSendWait) sendWait(player);
    }

    public static void sendInfo(Player player, Lang message) {
        send(player, ChatColor.AQUA + message.toString());
    }

    public static void sendWait(Player player) {
        send(player, ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + Lang.PLEASE_WAIT);
    }
}
