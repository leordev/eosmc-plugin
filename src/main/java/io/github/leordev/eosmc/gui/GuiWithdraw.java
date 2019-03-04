package io.github.leordev.eosmc.gui;

import io.github.leordev.eosmc.api.ApiWithdraw;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.items.ItemHelper;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class GuiWithdraw implements GuiEos {

    private final int WITHDRAW_SLOTS = 27;
    private boolean transacting = false;
    private ItemStack chainItem;
    private Player player;
    private Inventory gui;

    public GuiWithdraw(Player player, List<ItemStack> chainChest) {
        this.player = player;
        gui = Bukkit.createInventory(this, WITHDRAW_SLOTS, "Withdraw from Blockchain");
        initChest(chainChest);
    }

    private void initChest(List<ItemStack> chainChest) {
        int chestSlots = 1;
        for (ItemStack item : chainChest) {
            getInventory().addItem(item);

            chestSlots++;
            if (chestSlots > WITHDRAW_SLOTS) break;
        }
    }

    @Override
    public Inventory getInventory() {
        return gui;
    }

    @Override
    public void onGuiClick(InventoryClickEvent event) {
        if (checkTransactingAndBlock(event)) return;
        handleClickAction(event);
    }

    private boolean checkTransactingAndBlock(InventoryClickEvent event) {
        if (transacting || !isValidWithdrawAction(event.getAction())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private boolean isValidWithdrawAction(InventoryAction action) {
        return action == InventoryAction.PICKUP_ONE
                || action == InventoryAction.PICKUP_HALF
                || action == InventoryAction.PICKUP_ALL
                || action == InventoryAction.PICKUP_SOME
                || action == InventoryAction.PLACE_ALL
                || action == InventoryAction.PLACE_ONE
                || action == InventoryAction.PLACE_SOME;
    }

    private void handleClickAction(InventoryClickEvent event) {
        InventoryAction action = event.getAction();
        if (GuiHelper.isPlacing(action)) {
            handlePlacing(event);
        } else if (GuiHelper.isPicking(action)) {
            handlePicking(event);
        }
    }

    private void handlePlacing(InventoryClickEvent event) {
        boolean isPlayerSlot = event.getRawSlot() >= WITHDRAW_SLOTS;
        boolean isChainItem = !ItemHelper.isEmpty(chainItem);
        boolean isEmptySlot = ItemHelper.isEmpty(event.getCurrentItem());
        if (isPlayerSlot && isChainItem && isEmptySlot) {
            transferItemFromChain(event);
        } else if (!isPlayerSlot) {
            handleChainReturn(event);
        }
    }

    private void handlePicking(InventoryClickEvent event) {
        if (event.getRawSlot() < WITHDRAW_SLOTS) {
            chainItem = event.getCurrentItem();
        }
    }

    private void transferItemFromChain(InventoryClickEvent event) {

        if (!PlayerMetaData.validateAccountAndMessagePlayer(player)) {
            event.setCancelled(true);
            return;
        }

        try {
            ItemStack cursor = event.getCursor();
            int amount = event.getAction() == InventoryAction.PLACE_ONE ? 1 : cursor.getAmount();
            ItemStack item = new ItemStack(cursor);
            item.setAmount(amount);
            attemptTransfer(item);

            int rest = cursor.getAmount() - amount;
            if (rest == 0) {
                chainItem = ItemHelper.emptyItem();
            }
        } catch (Exception e) {
            handleTransferError(e, event);
        }
    }

    private void attemptTransfer(ItemStack item) throws IOException {
        transacting = true;
        MessageHelper.sendInfoAndWait(player, Lang.WT_ING);

        String account = PlayerMetaData.getEosAccount(player);
        ApiWithdraw.withdrawSingleItem(account, item);

        transacting = false;
        MessageHelper.sendSuccess(player,Lang.WT_SUCCESS);
    }

    private void handleTransferError(Exception e, InventoryClickEvent event) {
        e.printStackTrace();
        event.setCancelled(true);

        String reason = e instanceof IllegalArgumentException
                ? e.getMessage()
                : "Unknown Error";
        MessageHelper.sendError(player, Lang.WT_FAIL, reason);

        transacting = false;
    }

    private void handleChainReturn(InventoryClickEvent event) {
        boolean isChainItem = !ItemHelper.isEmpty(chainItem);
        boolean isPlacingAll = event.getAction() != InventoryAction.PLACE_ALL;

        if (!isChainItem || !isPlacingAll) {
            event.setCancelled(true);
            return;
        }

        chainItem = ItemHelper.emptyItem();
    }

    @Override
    public void onGuiClose() {
    }
}
