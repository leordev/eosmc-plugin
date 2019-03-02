package io.github.leordev.eosmc.player;

import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.utils.MessageHelper;
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
            MessageHelper.sendWarning(player, Lang.PLAYER_NO_ACC);
            MessageHelper.sendInfo(player,Lang.PLAYER_HELP_URL);
        } else {
            MessageHelper.sendSuccess(player,Lang.PLAYER_WELCOME, eosAccount);
        }

        MessageHelper.sendInfo(player,Lang.DP_HELP);
        MessageHelper.sendInfo(player,Lang.WT_HELP);

    }
}
