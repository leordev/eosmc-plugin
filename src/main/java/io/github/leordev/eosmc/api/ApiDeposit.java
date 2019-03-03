package io.github.leordev.eosmc.api;

import com.google.gson.JsonObject;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.items.TokenJson;
import io.github.leordev.eosmc.utils.HttpHandler;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class ApiDeposit {

    public static void depositBatch(String account, List<ItemStack> items) throws IOException {
        String url = EosConfig.getInterfaceServer() + "/player/" + account + "/deposit";
        JsonObject jsonItems = TokenJson.makeJsonItems(items);
        HttpHandler.postUrl(url, jsonItems.toString());
    }

}
