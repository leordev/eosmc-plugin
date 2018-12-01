package io.github.leordev;

//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;

import io.github.leordev.utils.HttpReader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

import static org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_OTHER;

public class EosMcPlugin extends JavaPlugin implements Listener {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable(){
        //Fired when the server enables the plugin
        getLogger().info(">>> leordev plugin enabling...");

        config.addDefault("greetings", "woooooooo");
        config.options().copyDefaults(true);
        saveConfig();

        // Enable our class to check for new players using onPlayerJoin()
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable(){
        //Fired when the server stops and disables all plugins
        getLogger().info(">>> leordev plugin DISABLED");
    }

    // This method checks for incoming players and sends them a message
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String greetings = config.getString("greetings");
        if (greetings != null && greetings.length() > 0) {
            player.sendMessage(">>>> " + greetings);
        } else {
            player.sendMessage("Oh no, admin didn't setup any greetings...");
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        getLogger().info(">>> dropping item by player: " + event.getPlayer().getDisplayName() + " - item: " + event.getItemDrop().getItemStack());

        Player player = event.getPlayer();

        try {
            ItemStack itemStack = event.getItemDrop().getItemStack();
            String url = "http://127.0.0.1:5051/drop/" + player.getName() + "/" + itemStack.getType().name() + "/" + itemStack.getAmount();
            String result = HttpReader.getUrl(url);
            if (result == null || result.indexOf("trxId") <= 0) {
                throw new Exception("failed chain transaction");
            }

            getLogger().info(">>> drop successfully: " + result);
        } catch (Exception e) {
            player.sendMessage("Error dropping item: " + e.getMessage());
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        getLogger().info(">>> picking up item by entity: " + event.getEntity() + " - item: " + event.getItem().getItemStack());

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            try {
                ItemStack itemStack = event.getItem().getItemStack();
                String url = "http://127.0.0.1:5051/pickup/" + player.getName() + "/" + itemStack.getType().name() + "/" + itemStack.getAmount();
                String result = HttpReader.getUrl(url);
                if (result == null || result.indexOf("trxId") <= 0) {
                    throw new Exception("failed chain transaction");
                }

                getLogger().info(">>> Pickup successfully: " + result);
            } catch (Exception e) {
                player.sendMessage("Error picking up item: " + e.getMessage());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        getLogger().info(">>> craft item by entity: " + event.getWhoClicked() + " - item: " + event.getRecipe().getResult() + " - craft inventory: " + event.getInventory().getMatrix());
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        getLogger().info(">>> PlayerItemConsumeEvent by entity: " + event.getPlayer() + " - item: " + event.getItem());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            try {
                for (ItemStack itemStack : event.getDrops()) {
                    String url = "http://127.0.0.1:5051/drop/" + player.getName() + "/" + itemStack.getType().name() + "/" + itemStack.getAmount();
                    String result = HttpReader.getUrl(url);
                    if (result == null || result.indexOf("trxId") <= 0) {
                        throw new Exception("failed chain transaction");
                    }

                    getLogger().info(">>> Drop successfully: " + result);
                }

            } catch (Exception e) {
                player.sendMessage("Error dropping items upon death: " + e.getMessage());
            }
        }
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) throws IOException {

        String playerName = event.getName();

        String url = "http://127.0.0.1:5051/account/" + playerName;
        try {
            String result = HttpReader.getUrl(url);

            if (result == null || result.indexOf("assets") <= 0) {
                throw new Exception("Player has no account");
            }
        } catch (Exception e) {
            getLogger().severe(">>> Player not allowed to login: " + playerName);
            getLogger().severe(e.getMessage());
            event.setLoginResult(KICK_OTHER);
            event.setKickMessage(e.getMessage());
        }
    }

}
