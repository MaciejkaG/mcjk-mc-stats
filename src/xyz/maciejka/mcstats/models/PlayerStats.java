package xyz.maciejka.mcstats.models;

public class PlayerStats {

    private String uuid;
    private String display_name;
    private int deaths;
    private int kills;
    private int mob_kills;
    private int blocks_broken;
    private int blocks_placed;
    private int time_played;

    public PlayerStats(String uuid, String display_name, int deaths, int kills, int mob_kills, int blocks_broken, int blocks_placed, int time_played) {
        this.uuid = uuid;
        this.display_name = display_name;
        this.deaths = deaths;
        this.kills = kills;
        this.mob_kills = mob_kills;
        this.blocks_broken = blocks_broken;
        this.blocks_placed = blocks_placed;
        this.time_played = time_played;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getMob_kills() {
        return mob_kills;
    }

    public void setMob_kills(int mob_kills) {
        this.mob_kills = mob_kills;
    }

    public int getBlocks_broken() {
        return blocks_broken;
    }

    public void setBlocks_broken(int blocks_broken) {
        this.blocks_broken = blocks_broken;
    }

    public int getBlocks_placed() {
        return blocks_placed;
    }

    public void setBlocks_placed(int blocks_placed) {
        this.blocks_placed = blocks_placed;
    }

    public int getTime_played() {
        return time_played;
    }

    public void setTime_played(int time_played) {
        this.time_played = time_played;
    }
}
