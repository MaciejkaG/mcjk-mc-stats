package xyz.maciejka.mcstats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import xyz.maciejka.mcstats.McStats;

import java.sql.SQLException;

public class ReloadDbCommand implements CommandExecutor {
    private final McStats plugin;

    public ReloadDbCommand(McStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            plugin.getDatabase().resetConnection();

            String msgContent = plugin.getConfig().getString("msdbreload-response");
            sender.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+msgContent);
            sender.sendMessage(ChatColor.YELLOW+"UWAGA: Reset połączenia z bazą danych nie wpłynie na załadowaną konfigurację pluginu. Aby ją przeładować, użyj komendy "+ChatColor.UNDERLINE+"/msreloadconfig"+ChatColor.UNDERLINE+".");
        } catch (SQLException e) {
            sender.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RED+"Reset połączenia z bazą danych nie powiódł się! Więcej informacji zostało wyświetlonych w konsoli serwera.");
            plugin.getLogger().severe(String.valueOf(e));
        }

        return true;
    }
}
