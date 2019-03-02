package io.github.leordev.eosmc.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHelper {
    public static void send(Player player, String message) {
        player.sendMessage(message);
    }

    public static void sendError(Player player, String message) {
        send(player, ChatColor.RED + message);
    }

    public static void sendSuccess(Player player, String message) {
        send(player, ChatColor.GREEN + message);
    }

    public static void sendWarning(Player player, String message) {
        send(player, ChatColor.GOLD + message);
    }

    public static void sendInfo(Player player, String message, boolean andSendWait) {
        send(player, ChatColor.AQUA + message);
        if (andSendWait) sendWait(player);
    }

    public static void sendInfo(Player player, String message) {
        send(player, ChatColor.AQUA + message);
    }

    public static void sendWait(Player player) {
        send(player, ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "Please wait...");
    }
}
