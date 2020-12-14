/**
 * Player Object, contains information about each player's name and score
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
class Player {

    /**
     * Player name
     */
    private String playerName;
    /**
     * Player score
     */
    private int playerScore;

    /**
     * Player Constructor
     *
     * @param playerName Player name
     */
    public Player(String playerName) {
        this.playerName = playerName;
        this.playerScore = 20;
    }

    /**
     * Increases player's score by 10
     */
    public void increaseScore() {
        playerScore += 10;
    }

    /**
     * Getter: Player Name
     *
     * @return Player Name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter: Player Score
     *
     * @return Player Score
     */
    public int getPlayerScore() {
        return playerScore;
    }
}