package io.github.leordev.eosmc.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.items.TokenHandler;
import io.github.leordev.eosmc.player.PlayerMetaData;
import io.github.leordev.eosmc.utils.HttpHandler;
import io.github.leordev.eosmc.utils.MessageHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    @Override
    public Inventory getInventory() {
        Inventory gui = Bukkit.createInventory(this, DEPOSIT_SLOTS, "Deposit to Blockchain");
        gui.setItem(CLOSE_SLOT, new ItemStack(Material.BARRIER));
        gui.setItem(CONFIRM_SLOT, new ItemStack(Material.KNOWLEDGE_BOOK));

        return gui;
    }

    @Override
    public void onGuiClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        ItemStack itemStack = event.getCurrentItem();

        if(isEmptyStack(itemStack)) return;

        if (slot == CLOSE_SLOT || slot == CONFIRM_SLOT) {
            event.setCancelled(true);
            if (!isEmptyStack(event.getCursor())) return;
        }

        if(slot == CLOSE_SLOT && !sendingToChain) {
            player.closeInventory();
        }

        if(slot == CONFIRM_SLOT && !sendingToChain) {
            submitBatchToChain(player, event);
        }
    }

    private void submitBatchToChain(Player player, InventoryClickEvent event) {
        List<ItemStack> items = getDepositBatchItems(event.getView());

        if (items.size() < 1) {
            MessageHelper.sendWarning(player, "No items to submit");
            return;
        }

        if (!PlayerMetaData.validateAccountAndMessagePlayer(player)) return;

        sendingToChain = true;
        MessageHelper.sendInfo(player, "Depositing to your EOS account", true);
        JsonArray jsonItems = new JsonArray();
        for (ItemStack itemStack : items) {
            jsonItems.add(makeJsonItem(itemStack));
        }
        JsonObject obj = new JsonObject();
        obj.add("items", jsonItems);

        try {
            String account = PlayerMetaData.getEosAccount(player);
            String url = EosConfig.getInterfaceServer() + "/player/" + account + "/deposit";
            HttpHandler.postUrl(url, obj.toString());
            removeBatchItems(event.getView());
            MessageHelper.sendSuccess(player,"Items were deposited to the chain successfully");
            player.closeInventory();
        } catch (IOException e) {
            e.printStackTrace();
            MessageHelper.sendError(player, "Fail to deposit items, please try again...");
            sendingToChain = false;
        }
    }

    private void removeBatchItems(InventoryView inventory) {
        for (int i = 0; i < DEPOSIT_SLOTS; i++) {
            if (i == CLOSE_SLOT || i == CONFIRM_SLOT) continue;
            ItemStack item = inventory.getItem(i);
            if (isEmptyStack(item)) continue;
            inventory.setItem(i, null);
        }
    }

    private JsonObject makeJsonItem(ItemStack itemStack) {
        JsonObject jsonItem = new JsonObject();
        String tokenName = TokenHandler.tokenizeItemName(itemStack.getType().name());
        jsonItem.addProperty("token_name", tokenName);
        jsonItem.addProperty("quantity", itemStack.getAmount());
        return jsonItem;
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

    @Override
    public void onGuiClose(Player player) {
        InventoryView inventory = player.getOpenInventory();
        List<ItemStack> recoveredItems = getDepositBatchItems(inventory);
        if (recoveredItems.size() > 0) {
            for (ItemStack item : recoveredItems) {
                player.getInventory().addItem(item);
            }
            player.updateInventory();
        }
    }

    boolean isEmptyStack(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(Material.AIR);
    }

}
