package com.company;

/**
 * User class
 * <p>This class contains information about the user</p>
 *
 * @author Keivan Ipchi Hagh & Bardia Ardakanian
 * @version 0.3.5
 */
public class User {

    /**
     * Objects, Variables, Components, ...
     */
    private String username;                                                            // username
    private String password;                                                            // Password
    private int killCount, deathCount, PVE_Wins, PVE_Loses, PVP_Wins, PVP_Loses;        // Details
    private long timePlayed;                                                            // Total played duration

    /**
     * Object Constructor
     *
     * @param username Username
     * @param password Password
     */
    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**
     * .toString()
     *
     * @return String
     */
    @Override
    public String toString() {
        String string = String.format("username=%s\npassword=%s\n" +
                        "kills=%d\ndeaths=%s\npveWins=%d\npveLoses=%d\npvpWins=%d\npvpLoses=%d\n" +
                        "timePlayed=%s", username, password, killCount, deathCount, PVE_Wins, PVE_Loses, PVP_Wins, PVP_Loses, time());
        return string;
    }

    /**
     * Played time (Formatted)
     *
     * @return String time
     */
    private String time() {
        int seconds = (int) (timePlayed / 1000) % 60;
        int minutes = (int) ((timePlayed / (1000 * 60)) % 60);
        int hours = (int) ((timePlayed / (1000 * 60 * 60)) % 24);
        return String.format("%03d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Setter
     *
     * @param username Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter
     *
     * @param password Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter
     *
     * @param timePlayed Played time
     */
    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    /**
     * Setter
     *
     * @param PVE_Wins PVE winds
     */
    public void setPVE_Wins(int PVE_Wins) {
        this.PVE_Wins = PVE_Wins;
    }

    /**
     * Setter
     *
     * @param PVE_Loses PVE loses
     */
    public void setPVE_Loses(int PVE_Loses) {
        this.PVE_Loses = PVE_Loses;
    }

    /**
     * Setter
     *
     * @param PVP_Wins PVP wins
     */
    public void setPVP_Wins(int PVP_Wins) {
        this.PVP_Wins = PVP_Wins;
    }

    /**
     * Setter
     *
     * @param PVP_Loses PVP loses
     */
    public void setPVP_Loses(int PVP_Loses) {
        this.PVP_Loses = PVP_Loses;
    }

    /**
     * Setter
     *
     * @param killCount Kill counts
     */
    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    /**
     * Setter
     *
     * @param deathCount Death count
     */
    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    /**
     * Getter
     *
     * @return Kill count
     */
    public int getKillCount() {
        return killCount;
    }

    /**
     * Getter
     *
     * @return Death count
     */
    public int getDeathCount() {
        return deathCount;
    }

    /**
     * Getter
     *
     * @return PVE winds
     */
    public int getPVE_Wins() {
        return PVE_Wins;
    }

    /**
     * Getter
     *
     * @return PVE loses
     */
    public int getPVE_Loses() {
        return PVE_Loses;
    }

    /**
     * Getter
     *
     * @return PVP winds
     */
    public int getPVP_Wins() {
        return PVP_Wins;
    }

    /**
     * Getter
     *
     * @return PVP wins
     */
    public int getPVP_Loses() {
        return PVP_Loses;
    }

    /**
     * Getter
     *
     * @return Total time played
     */
    public long getTimePlayed() {
        return timePlayed;
    }
}