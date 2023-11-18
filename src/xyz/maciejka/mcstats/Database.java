package xyz.maciejka.mcstats;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.maciejka.mcstats.models.PlayerStats;

import java.sql.*;
import java.util.Arrays;

public class Database {

    private final McStats plugin;
    private Connection connection;

    public Database(McStats plugin) {
        this.plugin = plugin;
    }

    public Connection getConnection() throws SQLException {
        if (connection!=null) {
            return connection;
        }

        FileConfiguration config = this.plugin.getConfig();

        String url = "jdbc:mysql://"+config.getString("mysql-host")+":"+config.getString("mysql-port")+"/"+config.getString("mysql-dbname");
        String user = config.getString("mysql-user");
        String password = config.getString("mysql-pass");

        try {
            connection = DriverManager.getConnection(url, user, password);

            return this.connection;
        } catch (SQLException e) {
            this.plugin.getLogger().info(ChatColor.RED+Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    public void initialiseDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();
        String query = "CREATE TABLE IF NOT EXISTS player_stats(uuid CHAR(36) PRIMARY KEY, display_name VARCHAR(16) NOT NULL, deaths INT UNSIGNED, kills INT UNSIGNED, mob_kills INT UNSIGNED, blocks_broken INT UNSIGNED, blocks_placed INT UNSIGNED, time_played INT UNSIGNED)";
        statement.execute(query);
        statement.close();
    }

    public PlayerStats findPlayerStatsByUUID(String uuid) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player_stats WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet results = statement.executeQuery();

        if (results.next()) {
            String displayName = results.getString("display_name");
            int deaths = results.getInt("deaths");
            int kills = results.getInt("kills");
            int mob_kills = results.getInt("mob_kills");
            int blocks_broken = results.getInt("blocks_broken");
            int blocks_placed = results.getInt("blocks_placed");
            int timePlayed = results.getInt("time_played");

            PlayerStats playerStats = new PlayerStats(uuid, displayName, deaths, kills, mob_kills, blocks_broken, blocks_placed, timePlayed);

            statement.close();

            return playerStats;
        }

        statement.close();

        return null;
    }

    public void createPlayerStats(PlayerStats stats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("INSERT INTO player_stats(uuid, display_name, deaths, kills, mob_kills, blocks_broken, blocks_placed, time_played) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, stats.getUuid());
        statement.setString(2, stats.getDisplay_name());
        statement.setInt(3, stats.getDeaths());
        statement.setInt(4, stats.getKills());
        statement.setInt(5, stats.getMob_kills());
        statement.setInt(6, stats.getBlocks_broken());
        statement.setInt(7, stats.getBlocks_placed());
        statement.setInt(8, stats.getTime_played());

        statement.executeUpdate();

        statement.close();
    }

    public void updatePlayerStats(PlayerStats stats) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement("UPDATE player_stats SET display_name = ?, deaths = ?, kills = ?, mob_kills = ?, blocks_broken = ?, blocks_placed = ?, time_played = ? WHERE uuid = ?");
        statement.setString(1, stats.getDisplay_name());
        statement.setInt(2, stats.getDeaths());
        statement.setInt(3, stats.getKills());
        statement.setInt(4, stats.getMob_kills());
        statement.setInt(5, stats.getBlocks_broken());
        statement.setInt(6, stats.getBlocks_placed());
        statement.setInt(7, stats.getTime_played());
        statement.setString(8, stats.getUuid());

        statement.executeUpdate();

        statement.close();
    }
}
