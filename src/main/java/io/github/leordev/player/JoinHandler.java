package io.github.leordev.player;

import io.github.leordev.config.EosConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler {
    public static void execute(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String account = EosConfig.getAccount();
        if (account != null && account.length() > 0) {
            player.sendMessage(">>> Welcome to EOS Craft provided by " + account);
            player.sendMessage(">>> Type: /dp to deposit items to the blockchain");
            player.sendMessage(">>> Type: /wt to withdraw items from the blockchain");
        } else {
            player.sendMessage("Oh no, admin didn't setup any EOS Account!");
        }
    }
}
