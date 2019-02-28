# EOS Minecraft Plugin

A plugin for SpigotMC to connect with EOS Blockchain

```java
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
        getLogger().info(">>> craft item by entity: "
                + event.getWhoClicked()
                + " - item: "
                + event.getRecipe().getResult()
                + " - craft inventory: "
                + event.getInventory().getMatrix());
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
```