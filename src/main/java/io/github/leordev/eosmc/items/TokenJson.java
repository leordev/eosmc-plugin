package io.github.leordev.eosmc.items;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TokenJson {

    public static JsonObject makeJsonItems(List<ItemStack> items) {
        JsonArray jsonItems = new JsonArray();
        for (ItemStack itemStack : items) {
            jsonItems.add(makeJsonItem(itemStack));
        }
        JsonObject obj = new JsonObject();
        obj.add("items", jsonItems);
        return obj;
    }

    public static JsonObject makeJsonItem(ItemStack itemStack) {
        TokenItem token = TokenHandler.fromItem(itemStack);
        JsonObject jsonItem = new JsonObject();
        jsonItem.addProperty("token_name", token.getEosTokenName());
        jsonItem.addProperty("quantity", itemStack.getAmount());
        jsonItem.addProperty("memo", itemStack.getItemMeta().toString());
        return jsonItem;
    }


}
