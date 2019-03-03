package io.github.leordev.eosmc.utils;

import io.github.leordev.eosmc.i18n.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHelper {

    public static void send(Player player,
                            ChatColor color,
                            Lang message,
                            Object... args) {
        String appendedMessage = String.format(message.toString(), args);
        player.sendMessage(color + appendedMessage);
    }

    public static void sendError(Player player, Lang message, Object... args) {
        send(player, ChatColor.RED, message, args);
    }

    public static void sendSuccess(Player player, Lang message, Object... args) {
        send(player, ChatColor.GREEN, message, args);
    }

    public static void sendWarning(Player player, Lang message, Object... args) {
        send(player, ChatColor.GOLD, message, args);
    }


    public static void sendInfo(Player player, Lang message, Object... args) {
        send(player, ChatColor.AQUA, message, args);
    }

    public static void sendInfoAndWait(Player player, Lang message, Object... args) {
        sendInfo(player, message, args);
        sendWait(player);
    }

    public static void sendWait(Player player) {
        send(player, ChatColor.LIGHT_PURPLE, Lang.PLEASE_WAIT);
    }
}
