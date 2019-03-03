package io.github.leordev.eosmc.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.items.TokenHandler;
import io.github.leordev.eosmc.items.TokenJson;
import io.github.leordev.eosmc.utils.HttpHandler;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiWithdraw {

    public static void withdrawSingleItem(String account, ItemStack item) throws IOException {
        List<ItemStack> items = new ArrayList<>();
        items.add(item);
        withdrawBatch(account, items);
    }

    public static void withdrawBatch(String account, List<ItemStack> items) throws IOException {
        String url = EosConfig.getInterfaceServer() + "/player/" + account + "/withdraw";
        JsonObject jsonItems = TokenJson.makeJsonItems(items);
        HttpHandler.postUrl(url, jsonItems.toString());
    }

    public static List<ItemStack> getChest(String account) throws IOException {
        String url = EosConfig.getInterfaceServer() + "/player/" + account;
        String response = HttpHandler.getUrl(url);
        JsonElement json = new JsonParser().parse(response);
        JsonArray chest = json.getAsJsonObject().get("chest").getAsJsonArray();

        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < chest.size(); i++) {
            JsonObject obj = chest.get(i).getAsJsonObject();
            ItemStack item = TokenHandler.fromTokenJson(obj);
            items.add(item);
        }

        return items;
    }
}
