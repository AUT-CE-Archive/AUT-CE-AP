/**
 * Player Object, contains information about each player's name and DNA
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Player {

    /**
     * Player's name
     */
    String playerName;
    /**
     * Players Cell DNA
     */
    Cell cell;

    /**
     * Obj Constructor
     * @param playerName player's name
     * @param cell player's cell DNA
     */
    public Player(String playerName, Cell cell) {
        this.playerName = playerName;
        this.cell = cell;
    }

    /**
     * Getter
     * @return player's cell DNA
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Getter
     * @return player's name
     */
    public String getPlayerName() {
        return playerName;
    }
}
