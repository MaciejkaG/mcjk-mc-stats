package xyz.maciejka.mcstats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import xyz.maciejka.mcstats.McStats;

public class ReloadCommand implements CommandExecutor {
    private final McStats plugin;

    public ReloadCommand(McStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        String msgContent = plugin.getConfig().getString("msreload-response");
        sender.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+msgContent);
        sender.sendMessage(ChatColor.YELLOW+"UWAGA: Przeładowanie konfiguracji nie wpłynie na połączenie z bazą danych, aby wprowadzić zmiany konfiguracji MySQL w życie, musisz zrestartować serwer.");
        return true;
    }
}
