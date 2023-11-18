package xyz.maciejka.mcstats.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
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

public class MyStatsCommand implements CommandExecutor {
    private final McStats plugin;

    public MyStatsCommand(McStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            FileConfiguration config = this.plugin.getConfig();

            p.sendMessage("Statystyki graczy są dostępny z poziomu przeglądarki");
            TextComponent message = new TextComponent("Kliknij tutaj by wyświetlić swoje statystyki w przeglądarce.");
            message.setColor(ChatColor.BLUE);
            message.setUnderlined(true);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, config.getString("web-stats-root")+"player.php?name="+URLEncoder.encode(p.getDisplayName(), StandardCharsets.UTF_8)));
            p.spigot().sendMessage(message);
        }

        return true;
    }
}
