package io.github.leordev.config;

import org.bukkit.configuration.file.FileConfiguration;

public class EosConfig {

    private static FileConfiguration instance;

    private static final String ACCOUNT = "eosAccount";
    private static final String PUBLIC_KEY = "eosPublicKey";
    private static final String PRIVATE_KEY = "eosPrivateKey";

    public static FileConfiguration initialize(FileConfiguration config) {
        config.addDefault(ACCOUNT, "eosminecraft");
        config.addDefault(PUBLIC_KEY, "eos-account-pub-key-here");
        config.addDefault(PRIVATE_KEY, "eos-account-priv-key-here");
        config.options().copyDefaults(true);
        instance = config;
        return instance;
    }

    public static String getAccount() {
        return instance.getString(ACCOUNT);
    }
}
