package xyz.maciejka.mcstats;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.maciejka.mcstats.models.PlayerStats;

import java.sql.SQLException;

public class Listeners implements Listener {

    private final McStats plugin;

    public Listeners(McStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) throws SQLException {
        Player p = e.getPlayer();

        PlayerStats stats = this.plugin.getDatabase().findPlayerStatsByUUID(p.getUniqueId().toString());
        this.plugin.getLogger().info("Block broken");

        if (stats==null) {
            stats = new PlayerStats(p.getUniqueId().toString(), p.getDisplayName(), 0, 0, 0, 1, 0, 0);

            this.plugin.getDatabase().createPlayerStats(stats);
            this.plugin.getLogger().info("Added player record");
        } else {
            stats.setBlocks_broken(stats.getBlocks_broken()+1);
            this.plugin.getDatabase().updatePlayerStats(stats);
            this.plugin.getLogger().info("Updated player record. Current blocks broken: "+stats.getBlocks_broken());
        }
    }
}
