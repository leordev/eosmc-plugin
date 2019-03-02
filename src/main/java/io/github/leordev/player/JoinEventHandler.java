package io.github.leordev.player;

import io.github.leordev.utils.MessageHelper;
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
            MessageHelper.sendWarning(player,"Ooopss... Looks like you didn't add your EOS Account yet.");
            MessageHelper.sendInfo(player,"Access http://eosminecraft.io/join to see instructions!");
        } else {
            MessageHelper.sendSuccess(player,"Welcome back to EOS Minecraft, " + eosAccount);
        }

        MessageHelper.sendInfo(player,"Use /dp to deposit items to the blockchain");
        MessageHelper.sendInfo(player,"Use /wt to withdraw items from the blockchain");

    }
}
