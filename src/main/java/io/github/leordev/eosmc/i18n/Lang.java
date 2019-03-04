package io.github.leordev.eosmc.i18n;

public enum Lang {
    ACC_HELP("acc-help", "Please setup your EOS Account with: /eosacc <account>"),
    ACC_SETTING("acc-setting", "Setting EOS Account"),
    ACC_SET("acc-set", "EOS account was set properly!"),
    ACC_SET_FAIL("acc-set-fail", "Fail to set EOS Account"),
    DP_GUI_TITLE("dp-gui-title", "Deposit to Blockchain"),
    DP_NO_ITEMS("dp-no-items", "No items to submit"),
    DP_HELP("dp-help", "Use /dp to deposit items to the blockchain"),
    DP_ING("dp-ing", "Depositing to your EOS account"),
    DP_SUCCESS("dp-success", "Items were deposited to the chain successfully"),
    DP_FAIL("dp-fail", "Fail to deposit items: %s"),
    PLAYER_NO_ACC("player-no-acc", "Ooopss... Looks like you didn't add your EOS Account yet."),
    PLAYER_HELP_URL("player-help-url", "Access http://eosminecraft.io/join to see instructions!"),
    PLAYER_WELCOME("player-welcome", "Welcome back to EOS Minecraft, %s"),
    PLEASE_WAIT("please-wait", "Please Wait..."),
    WT_HELP("wt-help", "Use /wt to withdraw items from the blockchain"),
    WT_GUI_TITLE("wt-gui-title", "Withdraw from Blockchain"),
    WT_ING("wt-ing", "Withdrawing item from your EOS Chest"),
    WT_SUCCESS("wt-success", "Items were transferred successfully"),
    WT_FAIL("wt-fail", "Fail to transfer item: %s"),
    WT_FAIL_ITEM("wt-fail-item", "Fail to load item(s): %s"),
    WT_FAIL_EMPTY("wt-fail-empty", "Your chest is empty, access: https://eosminecraft.io"),
    ;

    private final String path;
    private final String text;

    Lang(String path, String text) {
        this.path = path;
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
