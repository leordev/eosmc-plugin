package io.github.leordev.eosmc.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.utils.HttpHandler;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ApiAccount {

    public static boolean setAccount(Player player, String account) throws IOException {
        String mcUsername = player.getName();
        String data = "{\"mcUsername\": \"" + mcUsername + "\"}";

        String url = EosConfig.getInterfaceServer() + "/player/" + account + "/confirm";
        String response = HttpHandler.postUrl(url, data);
        JsonElement json = new JsonParser().parse(response);
        return json.getAsJsonObject().get("success").getAsBoolean();
    }
}
