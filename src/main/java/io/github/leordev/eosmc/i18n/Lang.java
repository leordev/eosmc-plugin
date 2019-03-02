package io.github.leordev.eosmc.i18n;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    ACC_HELP("acc-help", "Please setup your EOS Account with: /eosacc <account>"),
    ACC_SETTING("acc-setting", "Setting EOS Account"),
    ACC_SET("acc-set", "EOS account was set properly!"),
    ACC_SET_FAIL("acc-set-fail", "Fail to set EOS Account"),
    DP_NO_ITEMS("dp-no-items", "No items to submit"),
    DP_HELP("dp-help", "Use /dp to deposit items to the blockchain"),
    DP_ING("dp-ing", "Depositing to your EOS account"),
    DP_SUCCESS("dp-success", "Items were deposited to the chain successfully"),
    DP_FAIL("dp-fail", "Fail to deposit items, please try again..."),
    PLAYER_NO_ACC("player-no-acc", "Ooopss... Looks like you didn't add your EOS Account yet."),
    PLAYER_HELP_URL("player-help-url", "Access http://eosminecraft.io/join to see instructions!"),
    PLAYER_WELCOME("player-welcome", "Welcome back to EOS Minecraft, %1"),
    PLEASE_WAIT("please-wait", "Please Wait..."),
    WT_HELP("wt-help", "Use /wt to withdraw items from the blockchain");

    private static YamlConfiguration LANG;
    private final String path;
    private final String def;

    Lang(String path, String def) {
        this.path = path;
        this.def = def;
    }

    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }

    public String getDefault() {
        return this.def;
    }

    public String getPath() {
        return this.path;
    }
}
