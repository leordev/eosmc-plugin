package io.github.leordev.commands;

import io.github.leordev.items.TokenHandler;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import static io.github.leordev.EosMcPlugin.DATA_FOLDER;
import static io.github.leordev.EosMcPlugin.LOGGER;

public class CommandDump implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            return false;
        }

        String filePath = DATA_FOLDER.getAbsolutePath() + File.separator + "eos-items.csv";
        try (PrintStream out = new PrintStream(filePath)) {
            int count = 0;
            out.println("id,name");

            for (Material material : Material.values()) {
                if (!material.isItem()) continue;

                String itemLine = String.join(",",
                        String.valueOf(material.getId()),
                        TokenHandler.tokenizeItemName(material.name()));

                out.println(itemLine);
                count++;
            }

            LOGGER.info("Dumped " + count + " items.");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
