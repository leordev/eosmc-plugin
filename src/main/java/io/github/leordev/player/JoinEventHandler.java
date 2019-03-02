package io.github.leordev.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEventHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String eosAccount = PlayerMetaData.getEosAccount(player);

        if (eosAccount == null || eosAccount.isEmpty()) {
            player.sendMessage(">>> Ooopss... Looks like you didn't add your EOS Account yet.");
            player.sendMessage("Access http://eosminecraft.io/join to see instructions!");
        } else {
            player.sendMessage("Welcome back to EOS Minecraft, " + eosAccount);
        }

        player.sendMessage(">>> Type: /dp to deposit items to the blockchain");
        player.sendMessage(">>> Type: /wt to withdraw items from the blockchain");

    }
}
