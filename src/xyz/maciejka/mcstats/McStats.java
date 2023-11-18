package xyz.maciejka.mcstats;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.maciejka.mcstats.commands.LeaderboardCommand;
import xyz.maciejka.mcstats.commands.MyStatsCommand;
import xyz.maciejka.mcstats.commands.ReloadCommand;

import java.io.File;
import java.sql.SQLException;
import java.util.Objects;

public class McStats extends JavaPlugin {

    private Database database;

    @Override
    public void onEnable() {

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            getServer().getConsoleSender().sendMessage("["+getName()+"]"+ChatColor.YELLOW+" Utworzono domyślny plik konfiguracyjny, ponieważ nie istniał. Zmodyfikuj go, a następnie zrestartuj serwer, aby plugin zaczął działać.");
            getServer().getPluginManager().disablePlugin(this);
        } else {
            saveDefaultConfig();
            try {
                this.database = new Database(this);
                database.initialiseDatabase();

                getServer().getConsoleSender().sendMessage("["+getName()+"]"+ChatColor.GREEN+" Połączenie z bazą danych pomyślne.");
            } catch (SQLException e) {
                getServer().getConsoleSender().sendMessage("["+getName()+"]"+ChatColor.RED+" Połączenie z bazą danych nieudane.");
                throw new RuntimeException(e);
            }

            getServer().getPluginManager().registerEvents(new Listeners(this), this);
            Objects.requireNonNull(getCommand("msstats")).setExecutor(new MyStatsCommand(this));
            Objects.requireNonNull(getCommand("msleaderboard")).setExecutor(new LeaderboardCommand(this));
            Objects.requireNonNull(getCommand("msreloadconfig")).setExecutor(new ReloadCommand(this));
            getServer().getConsoleSender().sendMessage("["+getName()+"]"+ChatColor.AQUA+" Załadowano MaciejkaStats!");
        }
    }

    public Database getDatabase() {
        return database;
    }
}
