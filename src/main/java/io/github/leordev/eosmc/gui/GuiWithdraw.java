package io.github.leordev.eosmc.gui;

import io.github.leordev.eosmc.api.ApiWithdraw;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiWithdraw implements GuiEos {

    private final int WITHDRAW_SLOTS = 27;
    private boolean transacting = false;
    private Player player;
    private List<ItemStack> chainChest;

    public GuiWithdraw(Player player, List<ItemStack> chainChest) {
        this.player = player;
        this.chainChest = new ArrayList<>();
        initChest(chainChest);
    }

    private void initChest(List<ItemStack> chainChest) {
        for (int i = 0; i < chainChest.size(); i++) {
            ItemStack item = chainChest.get(i);
            getInventory().setItem(i, item);
            this.chainChest.add(item);
        }
    }

    @Override
    public Inventory getInventory() {
        Inventory gui = Bukkit.createInventory(this, WITHDRAW_SLOTS, "Withdraw from Blockchain");
        return gui;
    }

    public void transferItemFromChain(InventoryMoveItemEvent event) {

        if (transacting || !PlayerMetaData.validateAccountAndMessagePlayer(player)) {
            event.setCancelled(true);
            return;
        }

        try {
            ItemStack item = event.getItem();
            attemptTransfer(item);
        } catch (Exception e) {
            handleTransferError(e);
        }
    }

    private void attemptTransfer(ItemStack item) throws IOException {
        transacting = true;
        MessageHelper.sendInfoAndWait(player, Lang.WT_ING);

        String account = PlayerMetaData.getEosAccount(player);
        ApiWithdraw.withdrawSingleItem(account, item);

        transacting = false;
        MessageHelper.sendSuccess(player,Lang.WT_SUCCESS);
        player.closeInventory();
    }

    private void handleTransferError(Exception e) {
        e.printStackTrace();

        String reason = e instanceof IllegalArgumentException
                ? e.getMessage()
                : "Unknown Error";
        MessageHelper.sendError(player, Lang.WT_FAIL, reason);

        transacting = false;
    }

    public void rejectDeposits(InventoryMoveItemEvent event) {
        event.setCancelled(true);
        MessageHelper.sendError(player, Lang.WT_REJECTED);
    }

    @Override
    public void onGuiClick(InventoryClickEvent event) {
        if (transacting || GuiHelper.isDropping(event.getAction())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onGuiClose() {
    }
}
