package io.github.leordev.eosmc.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.i18n.Lang;
import io.github.leordev.eosmc.items.TokenHandler;
import io.github.leordev.eosmc.items.TokenItem;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.HttpHandler;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    private boolean sendingToChain = false;
    private Player player;

    public GuiDeposit(Player player) {
        this.player = player;
    }

    @Override
    public Inventory getInventory() {
        Inventory gui = Bukkit.createInventory(this, DEPOSIT_SLOTS, "Deposit to Blockchain");
        gui.setItem(CLOSE_SLOT, new ItemStack(Material.BARRIER));
        gui.setItem(CONFIRM_SLOT, new ItemStack(Material.KNOWLEDGE_BOOK));

        return gui;
    }

    @Override
    public void onGuiClick(InventoryClickEvent event) {

        if (event.getAction() == InventoryAction.DROP_ALL_CURSOR
            || event.getAction() == InventoryAction.DROP_ALL_SLOT
            || event.getAction() == InventoryAction.DROP_ONE_CURSOR
            || event.getAction() == InventoryAction.DROP_ONE_SLOT) {
            event.setCancelled(true);
            return;
        }

        ItemStack itemStack = event.getCurrentItem();
        if(isEmptyStack(itemStack)) return;

        int slot = event.getRawSlot();

        if (slot == CLOSE_SLOT || slot == CONFIRM_SLOT) {
            event.setCancelled(true);
            if (!isEmptyStack(event.getCursor())) return;
        }

        if(slot == CLOSE_SLOT && !sendingToChain) {
            player.closeInventory();
        }

        if(slot == CONFIRM_SLOT && !sendingToChain) {
            submitBatchToChain(event);
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
            if (isEmptyStack(item)) continue;
            items.add(item);
        }
        return items;
    }

    private void attemptBatchSubmission(List<ItemStack> items, InventoryView inventory) throws IOException {
        sendingToChain = true;
        MessageHelper.sendInfoAndWait(player, Lang.DP_ING);

        String account = PlayerMetaData.getEosAccount(player);
        String url = EosConfig.getInterfaceServer() + "/player/" + account + "/deposit";
        JsonObject jsonItems = makeJsonItems(items);
        HttpHandler.postUrl(url, jsonItems.toString());

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

        sendingToChain = false;
    }

    private JsonObject makeJsonItems(List<ItemStack> items) {
        JsonArray jsonItems = new JsonArray();
        for (ItemStack itemStack : items) {
            jsonItems.add(makeJsonItem(itemStack));
        }
        JsonObject obj = new JsonObject();
        obj.add("items", jsonItems);
        return obj;
    }

    private JsonObject makeJsonItem(ItemStack itemStack) {
        TokenItem token = TokenHandler.fromItem(itemStack);
        JsonObject jsonItem = new JsonObject();
        jsonItem.addProperty("token_name", token.getEosTokenName());
        jsonItem.addProperty("quantity", itemStack.getAmount());
        jsonItem.addProperty("memo", itemStack.getItemMeta().toString());
        return jsonItem;
    }

    private void removeBatchItems(InventoryView inventory) {
        for (int i = 0; i < DEPOSIT_SLOTS; i++) {
            if (i == CLOSE_SLOT || i == CONFIRM_SLOT) continue;
            ItemStack item = inventory.getItem(i);
            if (isEmptyStack(item)) continue;
            inventory.setItem(i, null);
        }
    }

    @Override
    public void onGuiClose() {
        InventoryView inventory = player.getOpenInventory();
        List<ItemStack> recoveredItems = getDepositBatchItems(inventory);
        if (recoveredItems.size() > 0) {
            for (ItemStack item : recoveredItems) {
                player.getInventory().addItem(item);
            }
            player.updateInventory();
        }
    }

    private boolean isEmptyStack(ItemStack itemStack) {
        return itemStack == null
                || itemStack.getType().equals(Material.AIR);
    }

}
