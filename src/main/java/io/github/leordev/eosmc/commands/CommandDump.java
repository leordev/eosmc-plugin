package io.github.leordev.eosmc.commands;

import io.github.leordev.eosmc.config.EosConfig;
import io.github.leordev.eosmc.items.TokenHandler;
import io.github.leordev.eosmc.items.TokenItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import static io.github.leordev.eosmc.EosMcPlugin.DATA_FOLDER;
import static io.github.leordev.eosmc.EosMcPlugin.LOGGER;

public class CommandDump implements CommandExecutor {

    private static final String HEADER_CSV = "src_id,src_name,category,token_name,fungible,burnable,transferable,max_supply";

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof ConsoleCommandSender)) {
            return false;
        }

        dumpCsv();
        dumpCleos();

        return true;
    }

    private void dumpCsv() {
        String filePath = DATA_FOLDER.getAbsolutePath() + File.separator + "eos-items.csv";
        try (PrintStream out = new PrintStream(filePath)) {
            out.println(HEADER_CSV);
            int count = printItems(out);
            LOGGER.info("Dumped " + count + " items.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int printItems(PrintStream out) {
        int count = 0;
        List<TokenItem> tokens = TokenHandler.tokens;
        for (TokenItem token : tokens) {
            String itemLine = dumpTokenCsv(token);
            out.println(itemLine);
            count++;
        }
        return count;
    }

    private String dumpTokenCsv(TokenItem item) {
        return String.join(",",
                String.valueOf(item.getSrcItemId()),
                item.getSrcItemName(),
                item.getEosCategory(),
                item.getEosTokenName(),
                String.valueOf(item.isFungible()),
                String.valueOf(item.isBurnable()),
                String.valueOf(item.isTransferable()),
                String.valueOf(item.getMaxSupply()));
    }

    private void dumpCleos() {
        String filePath = DATA_FOLDER.getAbsolutePath() + File.separator + "cleos-items.sh";
        try (PrintStream out = new PrintStream(filePath)) {
            out.println("set -e\nCLEOS=\"cleos -u http://localhost:8888 \"\n");
            printCleosItems(out);
            LOGGER.info("Dumped cleos items script.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void printCleosItems(PrintStream out) {
        List<TokenItem> tokens = TokenHandler.tokens;
        for (TokenItem token : tokens) {
            String cleosCmd = dumpTokenCleos(token);
            out.println(cleosCmd);
        }
    }

    private String dumpTokenCleos(TokenItem item) {
        String account = EosConfig.getAccount();
        return "$CLEOS push action "
                + account
                + " create '[\"" + account
                + "\", \"" + item.getEosCategory()
                + "\", \"" + item.getEosTokenName()
                + "\", " + item.isFungible()
                + ", " + item.isBurnable()
                + ", " + item.isTransferable()
                + ", " + item.getMaxSupply()
                + "]' -p " + account;
    }
}
