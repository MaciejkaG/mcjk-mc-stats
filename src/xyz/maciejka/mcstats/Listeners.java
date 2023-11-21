package xyz.maciejka.mcstats;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.maciejka.mcstats.models.PlayerStats;

import java.sql.SQLException;
import java.util.Objects;

public class Listeners implements Listener {

    private final McStats plugin;

    public Listeners(McStats plugin) {
        this.plugin = plugin;
    }

    private boolean isDatabaseUnavailable() throws SQLException {
        return !plugin.getDatabase().getConnection().isValid(5);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!p.hasPlayedBefore()) {
            FileConfiguration config = this.plugin.getConfig();

            p.sendMessage("Witaj na serwerze! Czy wiesz, że możesz łatwo uzyskać dostęp do statystyk swoich i innych graczy z poziomu strony internetowej?");
            TextComponent message = new TextComponent("Kliknij tutaj by przejść do strony głównej MaciejkaStats.");
            message.setColor(ChatColor.BLUE);
            message.setUnderlined(true);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, config.getString("web-stats-root")));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) throws SQLException {
        if (isDatabaseUnavailable()) {
            return;
        }

        Player p = e.getPlayer();

        PlayerStats stats = getPlayerStatsFromDatabase(p);

        stats.setTime_played(Math.round((float)p.getStatistic(Statistic.PLAY_ONE_MINUTE)/20/60));

        this.plugin.getDatabase().updatePlayerStats(stats);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) throws SQLException {
        if (isDatabaseUnavailable()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if(entity.getType() != EntityType.PLAYER && killer!=null) {
            PlayerStats stats = getPlayerStatsFromDatabase(killer);
            stats.setMob_kills(stats.getMob_kills()+1);

            FileConfiguration config = this.plugin.getConfig();
            int milestone = config.getInt("mobkills-milestone");
            int stat = stats.getMob_kills();

            if (milestone!=0 && stats.getMob_kills()%milestone==0) {
                killer.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+ Objects.requireNonNull(config.getString("mobkills-milestone-message")).replace("%player%", killer.getDisplayName()).replace("%stat%", Integer.toString(stat)));
            }

            this.plugin.getDatabase().updatePlayerStats(stats);
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) throws SQLException {
        if (isDatabaseUnavailable()) {
            return;
        }

        Player killer = e.getEntity().getKiller();
        Player victim = e.getEntity();

        if (killer == null) {
            PlayerStats victimStats = getPlayerStatsFromDatabase(victim);

            victimStats.setDeaths(victimStats.getDeaths()+1);

            FileConfiguration config = this.plugin.getConfig();
            int milestone = config.getInt("deaths-milestone");
            int stat = victimStats.getDeaths();

            if (milestone!=0 && victimStats.getDeaths()%milestone==0) {
                victim.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+ Objects.requireNonNull(config.getString("deaths-milestone-message")).replace("%player%", victim.getDisplayName()).replace("%stat%", Integer.toString(stat)));
            }

            this.plugin.getDatabase().updatePlayerStats(victimStats);

            return;
        }

        PlayerStats killerStats = getPlayerStatsFromDatabase(killer);
        PlayerStats victimStats = getPlayerStatsFromDatabase(victim);

        killerStats.setKills(killerStats.getKills()+1);
        victimStats.setDeaths(victimStats.getDeaths()+1);

        FileConfiguration config = this.plugin.getConfig();
        int dMilestone = config.getInt("deaths-milestone");
        int kMilestone = config.getInt("kills-milestone");
        int vStat = victimStats.getDeaths();
        int kStat = killerStats.getKills();

        if (dMilestone!=0 && vStat%dMilestone==0) {
            victim.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+ Objects.requireNonNull(config.getString("deaths-milestone-message")).replace("%player%", victim.getDisplayName()).replace("%stat%", Integer.toString(kStat)));
        }

        if (kMilestone!=0 && kStat%kMilestone==0) {
            killer.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+ Objects.requireNonNull(config.getString("kills-milestone-message")).replace("%player%", killer.getDisplayName()).replace("%stat%", Integer.toString(vStat)));
        }

        this.plugin.getDatabase().updatePlayerStats(killerStats);
        this.plugin.getDatabase().updatePlayerStats(victimStats);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent e) throws SQLException {
        if (isDatabaseUnavailable()) {
            return;
        }

        Player p = e.getPlayer();

        PlayerStats stats = getPlayerStatsFromDatabase(p);
        stats.setBlocks_placed(stats.getBlocks_placed()+1);

        FileConfiguration config = this.plugin.getConfig();
        int milestone = config.getInt("blocksplaced-milestone");
        int stat = stats.getBlocks_placed();

        if (milestone!=0 && stat%milestone==0) {
            p.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+ Objects.requireNonNull(config.getString("blocksplaced-milestone-message")).replace("%player%", p.getDisplayName()).replace("%stat%", Integer.toString(stat)));
        }

        this.plugin.getDatabase().updatePlayerStats(stats);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws SQLException {
        if (isDatabaseUnavailable()) {
            return;
        }

        Player p = e.getPlayer();

        PlayerStats stats = getPlayerStatsFromDatabase(p);
        stats.setBlocks_broken(stats.getBlocks_broken()+1);

        FileConfiguration config = this.plugin.getConfig();
        int milestone = config.getInt("blocksbroken-milestone");
        int stat = stats.getBlocks_broken();

        if (milestone!=0 && stat%milestone==0) {
            p.sendMessage(ChatColor.AQUA+"[MaciejkaStats] "+ChatColor.RESET+ Objects.requireNonNull(config.getString("blocksbroken-milestone-message")).replace("%player%", p.getDisplayName()).replace("%stat%", Integer.toString(stat)));
        }

        this.plugin.getDatabase().updatePlayerStats(stats);
    }

    private PlayerStats getPlayerStatsFromDatabase(Player p) throws SQLException {
        PlayerStats stats = this.plugin.getDatabase().findPlayerStatsByUUID(p.getUniqueId().toString());

        if (stats==null) {
            stats = new PlayerStats(p.getUniqueId().toString(), p.getDisplayName(), 0, 0, 0, 0, 0, 0);

            this.plugin.getDatabase().createPlayerStats(stats);
        }

        return stats;
    }
}
