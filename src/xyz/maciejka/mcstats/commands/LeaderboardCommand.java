package xyz.maciejka.mcstats.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import xyz.maciejka.mcstats.McStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class LeaderboardCommand implements CommandExecutor {
    private final McStats plugin;

    public LeaderboardCommand(McStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            FileConfiguration config = this.plugin.getConfig();

            p.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+config.getString("msleaderboard-response.first-line"));
            TextComponent message = new TextComponent(config.getString("msleaderboard-response.link-line"));
            message.setColor(ChatColor.BLUE);
            message.setUnderlined(true);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, config.getString("web-stats-root")+"leaderboard.php"));
            p.spigot().sendMessage(message);
        }

        return true;
    }
}
