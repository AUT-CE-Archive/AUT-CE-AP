import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Game Object, contains all objects and methods to manipulate the data and to
 * update the board as well as players' records
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
class Game {

    /**
     * Board Obj
     */
    private Board board;
    /**
     * Players' Obj
     */
    private Player player1, player2;
    /**
     * A list containing the most-recent game options
     */
    private List<Cell> options = new ArrayList<>();
    /**
     * Other words for cycleIndex, indicates which player's turn it is
     */
    private int turn;
    /**
     * Local variable
     */
    private int count = 0;
    /**
     * Local variable
     */
    private boolean isBreak = false;
    /**
     * GUI Indicator
     */
    private boolean GUI;
    /**
     * Scanner
     */
    public Scanner scanner;
    /**
     * The AI
     */
    private AI ai;
    /**
     * Game Mode
     */
    String gameMode;
    /**
     * AI difficulty
     */
    String difficulty;

    /**
     * #1 Game Constructor (Primary)
     *
     * @param player1Name Player Name
     * @param player2Name Player Name
     */
    public Game(String player1Name, String player2Name, boolean GUI, String gameMode, String difficulty) {

        // Initialize Board, Players and Reset fields
        board = new Board(8);
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        turn = 0;
        this.GUI = GUI;
        this.difficulty = difficulty;
        scanner = new Scanner(System.in);

        // Set game mode
        this.gameMode = gameMode;

        // Show the primary map
        setOptions();

        // Draw visual map
        board.draw(GUI);

        // Initialize AI
        ai = new AI();
    }

    /**
     * #2 Game Constructor (Secondary)
     *
     * Does nothing, just to make Inheritance possible (:
     */
    public Game() { }

    /**
     * This method finds out possible moves and prints them as yellow dots, then
     * draws the visual map
     */
    private void setOptions() {
        Cell[][] map = board.getMap();
        board.resetOptions(); // Reset Previous Options
        options.clear();

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++)
                if (map[i][j].getType() != CellType.EMPTY && map[i][j].getType() != CellType.OPTION)
                    if (turn % 2 == 0 && map[i][j].getType() == CellType.BLACK) // Black player
                        options.addAll(getOptions(i, j, map, CellType.WHITE));
                    else if (turn % 2 == 1 && map[i][j].getType() == CellType.WHITE) // White player
                        options.addAll(getOptions(i, j, map, CellType.BLACK));

        for (Cell option : options) {
            map[option.i][option.j].setType(CellType.OPTION);
        }
    }

    /**
     * Handles singleplayer/multiplayer modes
     */
    public void play() throws InterruptedException {

        if (gameMode.equals("ai_vs_ai"))
            while (!checkGameOver()) {
                setOptions();
                String decision = ai.decide(board.getMap(), options, "medium");
                placeDisc(decision);
                increaseTurn();
                increaseTurn();
                Thread.sleep(500);
            }
        else if (gameMode.equals("singleplayer"))
            while (!checkGameOver()) {

                System.out.print("- " + getCurrentTurn_PlayerName() + ", Enter Coordinates (i.e. '3 D' for 'X = D & Y = 3'): ");
                String input = scanner.nextLine();
                boolean isValid = isMoveValid(input);
                placeDisc(input);

                if  (isValid) {
                    setOptions();
                    String decision = ai.decide(board.getMap(), options, difficulty);
                    placeDisc(decision);
                    increaseTurn();
                    increaseTurn();
                    Thread.sleep(200);
                }
            }
        else
            while (!checkGameOver()) {
                System.out.print("- " + getCurrentTurn_PlayerName() + ", Enter Coordinates (i.e. '3 D' for 'X = D & Y = 3'): ");
                placeDisc(scanner.nextLine());
            }

        board.draw(GUI);
        System.out.println("\033[0;92m GAME OVER!" + getWinner() + " \033[0m");
    }

    /**
     * this method validates user inputs, places a disk for whichever player
     * currently has the turn, changes corresponding disks accordingly and suggests
     * new options; Finally, updates the entire map
     *
     * @param input User input
     */
    private void placeDisc(String input) {

        if (isMoveValid(input)) {

            String row = input.split(" ")[0];
            String column = input.split(" ")[1].toUpperCase();            

            // Get targeted coordinates
            int rowID = Integer.parseInt(row) - 1;
            int columnID = ((int) column.charAt(0)) - 65;

            // Get targeted cell
            Cell cell = board.getCell(rowID, columnID);

            // Change type of target cell
            if (turn % 2 == 0)
                cell.setType(CellType.BLACK);
            else
                cell.setType(CellType.WHITE);

            // Apply changes to the board
            board.changeCell(cell, rowID, columnID);

            // Change corresponding modifies
            Cell[][] map = board.getMap();
            if (cell.getType() == CellType.BLACK)
                modify(rowID, columnID, map, CellType.WHITE);
            else
                modify(rowID, columnID, map, CellType.BLACK);

            // Go to other player's turn
            turn++;
        }

        // Set options
        setOptions();        

        if (checkGameOver()) {
            turn = -1;
            return;
        }

        // Check if next player has any options to move, if not, then move on to the
        // other player
        if (checkPass()) {

            String name = "";
            if (turn % 2 == 0)
                name = player1.getPlayerName();
            else
                name = player2.getPlayerName();

            System.out.println(ConsoleColors.RED_BRIGHT
                    + "\'OutOfOptions exception\' thrown by Game_ExceptionHandler (\'" + name + "\' Passed - No moves available)"
                    + ConsoleColors.RESET);
            turn++;
            setOptions();
        }

        // Draw visual map
        board.draw(GUI);

        // Print players' scores, turn, ...
        System.out.print(ConsoleColors.BLUE_BRIGHT + player1.getPlayerName() + " Score: \'" + player1.getPlayerScore()
                + "\' " + ConsoleColors.RESET + "\t\t");
        System.out.print(ConsoleColors.BLUE_BRIGHT + player2.getPlayerName() + " Score: \'" + player2.getPlayerScore()
                + "\' " + ConsoleColors.RESET + "\t\t");
        System.out.println(ConsoleColors.BLUE_BRIGHT + "Cycle: \'" + turn + "\'" + ConsoleColors.RESET);
    }

    /**
     * Check if player is PASS or not
     * 
     * @return is player PASS?
     */
    private boolean checkPass() {
        Cell[][] map = board.getMap();

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++)
                if (map[i][j].getType() == CellType.OPTION)
                    return false;
        return true;
    }

    /**
     * Check if game is over
     * 
     * @return is game over?
     */
    private boolean checkGameOver() {
        Cell[][] map = board.getMap();
        
        int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0;

        for (Cell[] cells : map)
            for (int j = 0; j < map.length; j++)
                switch (cells[j].getType()) {
                    case EMPTY:
                        cnt1++;
                        break;
                    case OPTION:
                        cnt2++;
                        break;
                    case WHITE:
                        cnt3++;
                        break;
                    case BLACK:
                        cnt4++;
                        break;
                }

        boolean c1 = checkPass();
        increaseTurn();
        boolean c2 = checkPass();
        turn--;

        return cnt3 == 0 || cnt4 == 0 || (cnt2 == 0 && cnt1 == 0) || (c1 && c2);
    }

    /**
     * This method indicates whether users input is valid or not by checking if
     * input is outside bounds, Invalid, ...
     *
     * @param input User input
     * @return Boolean indicating whether move is OK or not
     */
    private boolean isMoveValid(String input) {
        try {
            String row = input.split(" ")[0];
            String column = input.split(" ")[1].toUpperCase();            

            // Convert row and column into Integer to see if they have the valid format or
            // not
            int rowID = Integer.parseInt(row) - 1;
            int columnID = ((int) column.charAt(0)) - 65;

            // Check whether user has selected among the options or not
            boolean contains = false;
            for (Cell cell: options) {
                Cell mapCell = board.getMap()[rowID][columnID];
                if (cell.getType() == mapCell.getType())
                    contains = true;
            }

            if (contains)
                return true;
            else {
                System.out.println(ConsoleColors.RED_BRIGHT
                        + "\'InvalidOptionSelection exception\' thrown by Game_ExceptionHandler (Select only the available options)"
                        + ConsoleColors.RESET);
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ConsoleColors.RED_BRIGHT
                    + "\'IncorrectInputFormat/OutsideBounds exception\' thrown by Game_ExceptionHandler (See Rules for more information)"
                    + ConsoleColors.RESET);
        }
        return false;
    }

    /**
     * This method returns the possible options for each player
     *
     * @param i    Indexer
     * @param j    Indexer
     * @param map  Board map
     * @param type Cell type
     * @return list of possible options
     */
    private List<Cell> getOptions(int i, int j, Cell[][] map, CellType type) {
        List<Cell> list = new ArrayList<>();

        // Right
        count = 0;
        isBreak = false;
        for (int k = i, t = j + 1; t < map.length; t++) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        // Left
        count = 0;
        isBreak = false;
        for (int k = i, t = j - 1; t >= 0; t--) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }
        // Down
        count = 0;
        isBreak = false;
        for (int k = i + 1, t = j; k < map.length; k++) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        // Up
        count = 0;
        isBreak = false;
        for (int k = i - 1, t = j; k >= 0; k--) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        // Up-Left
        count = 0;
        isBreak = false;
        for (int k = i - 1, t = j - 1; k >= 0 && t >= 0; k--, t--) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        // Up-Right
        count = 0;
        isBreak = false;
        for (int k = i - 1, t = j + 1; k >= 0 && t < map.length; k--, t++) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        // Down-Left
        count = 0;
        isBreak = false;
        for (int k = i + 1, t = j - 1; k < map.length && t >= 0; k++, t--) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        // Down-Right
        count = 0;
        isBreak = false;
        for (int k = i + 1, t = j + 1; k < map.length && t < map.length; k++, t++) {
            Cell cell = addOptionsList(k, t, map, type);
            if (cell.getType() == CellType.OPTION)
                list.add(cell);
            if (isBreak)
                break;
        }

        return list;
    }

    /**
     * Encapsulates the duplicate codes for option selection
     *
     * @param k    Indexer
     * @param t    Indexer
     * @param map  Board amp
     * @param type Cell type
     * @return The cell that is considered an option for the player to move on
     */
    private Cell addOptionsList(int k, int t, Cell[][] map, CellType type) {

        Cell cell = new Cell(CellType.EMPTY, k, t);

        if (map[k][t].getType() == type)
            count++;
        else if (map[k][t].getType() == CellType.EMPTY && count > 0) {
            cell.setType(CellType.OPTION);
            isBreak = true;
        } else
            isBreak = true;

        return cell;
    }

    /**
     * Encapsulates the duplicate codes for cell modifications
     *
     * @param k    Indexer
     * @param t    Indexer
     * @param map  Board map
     * @param type Cell type
     * @param list A list of all awaiting cells for modification
     * @param temp Temporary awaiting cells for modification
     */
    protected void addModification(int k, int t, Cell[][] map, CellType type, List<Cell> list, List<Cell> temp) {
        if (map[k][t].getType() == type)
            temp.add(map[k][t]);
        else if (map[k][t].getType() != CellType.EMPTY && map[k][t].getType() != CellType.OPTION)
            list.addAll(temp);
    }

    /**
     * This method modifies the cells in the middle after player's move (See Ruls)
     *
     * @param i    Indexer
     * @param j    Indexer
     * @param map  Board map
     * @param type Cell type
     */
    private void modify(int i, int j, Cell[][] map, CellType type) {

        List<Cell> list = new ArrayList<>();
        List<Cell> temp = new ArrayList<>();

        // Right
        isBreak = false;
        temp.clear();
        for (int k = i, t = j + 1; t < map.length; t++)
            addModification(k, t, map, type, list, temp);

        // Left
        isBreak = false;
        temp.clear();
        for (int k = i, t = j - 1; t >= 0; t--)
            addModification(k, t, map, type, list, temp);

        // Down
        isBreak = false;
        temp.clear();
        for (int k = i + 1, t = j; k < map.length; k++)
            addModification(k, t, map, type, list, temp);

        // Up
        isBreak = false;
        temp.clear();
        for (int k = i - 1, t = j; k >= 0; k--)
            addModification(k, t, map, type, list, temp);

        // Up-Left
        isBreak = false;
        temp.clear();
        for (int k = i - 1, t = j - 1; k >= 0 && t >= 0; k--, t--)
            addModification(k, t, map, type, list, temp);

        // Up-Right
        isBreak = false;
        temp.clear();
        for (int k = i - 1, t = j + 1; k >= 0 && t < map.length; k--, t++)
            addModification(k, t, map, type, list, temp);

        // Down-Left

        isBreak = false;
        temp.clear();
        for (int k = i + 1, t = j - 1; k < map.length && t >= 0; k++, t--)
            addModification(k, t, map, type, list, temp);

        // Down-Right
        isBreak = false;
        temp.clear();
        for (int k = i + 1, t = j + 1; k < map.length && t < map.length; k++, t++)
            addModification(k, t, map, type, list, temp);

        for (Cell cell : list) {
            if (type == CellType.WHITE)
                cell.setType(CellType.BLACK);
            else
                cell.setType(CellType.WHITE);

            board.changeCell(cell, cell.i, cell.j);

            // Increase player's score by 10 for each modification
            if (turn % 2 == 0)
                player1.increaseScore();
            else
                player2.increaseScore();
        }
    }

    /**
     * Return the name of the player which has the turn
     *
     * @return Player name
     */
    public String getCurrentTurn_PlayerName() {
        if (turn % 2 == 0)
            return player1.getPlayerName() + " (GUI: Blue, Console: Black)";
        else
            return player2.getPlayerName() + " (GUI: Red, Console: White)";
    }

    /**
     * Getter: Get turn
     * 
     * @return the turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Return winner's name
     * @return Winner's name
      */
    public String getWinner() {
        if (player1.getPlayerScore() > player2.getPlayerScore())
            return " - WE HAVE A WINNER: " + player1.getPlayerName();
        else if (player1.getPlayerScore() < player2.getPlayerScore())
            return " - WE HAVE A WINNER: " + player2.getPlayerName();
        else
            return " - WE HAVE NO WINNER!: GAME DRAW!!!! ";
    }

    /**
     * Increases turn
     */
    protected void increaseTurn() {
        turn++;
    }
}