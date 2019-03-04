package io.github.leordev.eosmc.gui;

import io.github.leordev.eosmc.api.ApiDeposit;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.items.ItemHelper;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiDeposit implements GuiEos {

    private final int CLOSE_SLOT = 29;
    private final int CONFIRM_SLOT = 33;
    private final int DEPOSIT_SLOTS = 36;
    private boolean transacting = false;
    private Player player;
    private Inventory gui;

    public GuiDeposit(Player player) {
        this.player = player;
        initInventory();
    }

    private void initInventory() {
        gui = Bukkit.createInventory(this, DEPOSIT_SLOTS, Lang.DP_GUI_TITLE.toString());
        gui.setItem(CLOSE_SLOT, new ItemStack(Material.BARRIER));
        gui.setItem(CONFIRM_SLOT, new ItemStack(Material.KNOWLEDGE_BOOK));
    }

    @Override
    public Inventory getInventory() {
        return gui;
    }

    @Override
    public void onGuiClick(InventoryClickEvent event) {
        if (checkIsTransactingOrDroppingAndCancel(event)) return;
        handleActions(event);
    }

    private boolean checkIsTransactingOrDroppingAndCancel(InventoryClickEvent event) {
        if (transacting || GuiHelper.isDropping(event.getAction())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private void handleActions(InventoryClickEvent event) {
        int slot = event.getRawSlot();

        boolean isClosingWindow = slot == CLOSE_SLOT;
        boolean isConfirmingBatch = slot == CONFIRM_SLOT;

        if (isClosingWindow || isConfirmingBatch) {
            event.setCancelled(true);

            boolean isHoldingItem = !ItemHelper.isEmpty(event.getCursor());
            if (isHoldingItem) return;

            if (isConfirmingBatch) {
                submitBatchToChain(event);
            } else {
                player.closeInventory();
            }
        }
    }

    private void submitBatchToChain(InventoryClickEvent event) {
        List<ItemStack> items = getDepositBatchItems(event.getView());

        if (items.size() < 1) {
            MessageHelper.sendWarning(player, Lang.DP_NO_ITEMS);
            return;
        }

        if (!PlayerMetaData.validateAccountAndMessagePlayer(player)) return;

        try {
            attemptBatchSubmission(items, event.getView());
        } catch (Exception e) {
            handleBatchSubmissionError(e);
        }
    }

    private List<ItemStack> getDepositBatchItems(InventoryView inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < DEPOSIT_SLOTS; i++) {
            if (i == CLOSE_SLOT || i == CONFIRM_SLOT) continue;
            ItemStack item = inventory.getItem(i);
            if (ItemHelper.isEmpty(item)) continue;
            items.add(item);
        }
        return items;
    }

    private void attemptBatchSubmission(List<ItemStack> items, InventoryView inventory) throws IOException {
        transacting = true;
        MessageHelper.sendInfoAndWait(player, Lang.DP_ING);

        String account = PlayerMetaData.getEosAccount(player);
        ApiDeposit.depositBatch(account, items);

        MessageHelper.sendSuccess(player,Lang.DP_SUCCESS);
        removeBatchItems(inventory);
        player.closeInventory();
    }

    private void handleBatchSubmissionError(Exception e) {
        e.printStackTrace();

        String reason = e instanceof IllegalArgumentException
                ? e.getMessage()
                : "Unknown Error";
        MessageHelper.sendError(player, Lang.DP_FAIL, reason);

        transacting = false;
    }

    private void removeBatchItems(InventoryView inventory) {
        for (int i = 0; i < DEPOSIT_SLOTS; i++) {
            if (i == CLOSE_SLOT || i == CONFIRM_SLOT) continue;
            ItemStack item = inventory.getItem(i);
            if (ItemHelper.isEmpty(item)) continue;
            inventory.setItem(i, null);
        }
    }

    @Override
    public void onGuiClose(InventoryCloseEvent event) {
        InventoryView inventory = player.getOpenInventory();
        List<ItemStack> recoveredItems = getDepositBatchItems(inventory);
        if (recoveredItems.size() > 0) {
            for (ItemStack item : recoveredItems) {
                player.getInventory().addItem(item);
            }
            player.updateInventory();
        }
    }

}
