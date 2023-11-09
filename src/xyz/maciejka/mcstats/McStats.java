package xyz.maciejka.mcstats;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class McStats extends JavaPlugin {

    private Database database;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        try {
            this.database = new Database(this);
            database.initialiseDatabase();

            getLogger().info("Połączenie z bazą danych pomyślne.");
        } catch (SQLException e) {
            getLogger().info("Połączenie z bazą danych nieudane.");
            throw new RuntimeException(e);
        }

        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        getLogger().info("Załadowano MaciejkaStats");
    }

    public Database getDatabase() {
        return database;
    }
}
