package io.github.leordev.player;

import io.github.leordev.EosMcPlugin;
import io.github.leordev.config.EosConfig;
import io.github.leordev.utils.MessageHelper;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class PlayerMetaData {

    public static boolean validateAccountAndMessagePlayer(Player player) {
        String account = PlayerMetaData.getEosAccount(player);
        if (account == null || account.isEmpty()) {
            MessageHelper.sendWarning(player, "Please setup your EOS Account with: /eosacc <account>");
        }

        return true;
    }

    public static String getEosAccount(Player player) {
        String eosAccount = "";
        if (player.hasMetadata(EosConfig.ACCOUNT)) {
            List<MetadataValue> mdValues = player.getMetadata(EosConfig.ACCOUNT);
            for (MetadataValue mdValue : mdValues) {
                if (mdValue.getOwningPlugin() == EosMcPlugin.instance) {
                    eosAccount = mdValue.asString();
                }
            }
        }

        return (eosAccount != null && !eosAccount.isEmpty()) ? eosAccount : "";
    }

    public static void setEosAccount(Player player, String account) {
        player.setMetadata(EosConfig.ACCOUNT, new FixedMetadataValue(EosMcPlugin.instance, account));
    }
}
