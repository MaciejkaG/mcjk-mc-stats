package xyz.maciejka.mcstats.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.maciejka.mcstats.McStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReloadCommand implements CommandExecutor {
    private final McStats plugin;

    public ReloadCommand(McStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN+"Pomyślnie przeładowano konfigurację pluginu!");
        sender.sendMessage(ChatColor.YELLOW+"UWAGA: Przeładowanie konfiguracji nie wpłynie na połączenie z baza danych, aby wprowadzić zmiany konfiguracji MySQL w życie, musisz zrestartować serwer.");
        return true;
    }
}
