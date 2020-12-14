/**
 * Game Engine, handles most of the stuff
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
import java.util.Random;
import java.util.Scanner;

public class Pentago {

    /**
     * Players
     */
    private Player player1, player2;
    /**
     * Board Obj
     */
    private Board board;
    /**
     * Player turn Indicator
     */
    private int turn;
    /**
     * GUI Indicator
     */
    private boolean GUI;
    /**
     * Game Mode
     */
    private String gameMode;
    /**
     * AI difficulty
     */
    private String difficulty;
    /**
     * Scanner Obj
     */
    private Scanner scanner;
    /**
     * AI Onj
     */
    private AI ai;

    /**
     * Obj Constructor
     * @param player1Name player name
     * @param player2Name player name
     */
    public Pentago(String player1Name, String player2Name, boolean GUI, String gameMode, String difficulty) {

        Random random = new Random();
        turn  = 0;
        this.GUI = GUI;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.scanner = new Scanner(System.in);
        this.ai = new AI();

        // Initialize players with random CellTypes
        if (random.nextInt(2) == 0) {
            player1 = new Player(player1Name, new Cell(ConsoleColors.GREEN));
            player2 = new Player(player2Name, new Cell(ConsoleColors.RED));
        } else {
            player1 = new Player(player1Name, new Cell(ConsoleColors.RED));
            player2 = new Player(player2Name, new Cell(ConsoleColors.GREEN));
        }

        // Initialize Board
        board = new Board(3);

        display(GUI);
    }

    /**
     * does as it says
     */
    public void display(boolean GUI) {
        board.draw(GUI);
    }

    /**
     * Handles pretty much all other methods
     */
    public void play() throws InterruptedException {
        if (gameMode.equals("ai_vs_ai")) {
            while (turn != -1) {
                String decision;
                if (turn % 2 == 0)
                    decision = ai.decide(board.getFullBoard(), "medium", player1);
                else
                    decision = ai.decide(board.getFullBoard(), "medium", player2);

                System.out.println("- " + getPlayerTurnName() + ", Has made a move: " + decision);
                placeDisc(decision);
                Thread.sleep(50);

                if (turn == -1)
                    break;
            }
        } else if (gameMode.equals("singleplayer"))
            while (turn != -1) {

                System.out.print("- " + getPlayerTurnName() + ", Enter " + ConsoleColors.RED_BOLD_BRIGHT + "<X> <Y> <QuadrantPos> <clockWiseOrNot>" + ConsoleColors.RESET + " (i.e. " + ConsoleColors.GREEN_BOLD_BRIGHT + "\'<X> <Y> TL true\'" + ConsoleColors.RESET + ", " + ConsoleColors.GREEN_BOLD_BRIGHT + "\'<X> <Y> BR false\'" + ConsoleColors.RESET + ", (" + ConsoleColors.PURPLE_BOLD_BRIGHT + "\'No-Rotation Disabled - See RULES\'" + ConsoleColors.RESET + ") where" + ConsoleColors.BLUE_BOLD_BRIGHT + " (TL: Top-Left, BR: Bottom-Right, ...)" + ConsoleColors.RESET + "): ");
                String input = scanner.nextLine();
                boolean isValid = isInputOK(input);

                if  (isValid) {

                    placeDisc(input);

                    if (turn != -1) {
                        String decision = ai.decide(board.getFullBoard(), difficulty, player2);
                        System.out.println("- " + getPlayerTurnName() + ", Has made a move: " + decision);
                        placeDisc(decision);
                        Thread.sleep(200);
                    }
                }
            }
        else
            while (turn != -1) {
                System.out.print("- " + getPlayerTurnName() + ", Enter " + ConsoleColors.RED_BOLD_BRIGHT + "<X> <Y> <QuadrantPos> <clockWiseOrNot>" + ConsoleColors.RESET + " (i.e. " + ConsoleColors.GREEN_BOLD_BRIGHT + "\'<X> <Y> TL true\'" + ConsoleColors.RESET + ", " + ConsoleColors.GREEN_BOLD_BRIGHT + "\'<X> <Y> BR false\'" + ConsoleColors.RESET + ", (" + ConsoleColors.PURPLE_BOLD_BRIGHT + "\'No-Rotation Disabled - See RULES\'" + ConsoleColors.RESET + ") where" + ConsoleColors.BLUE_BOLD_BRIGHT + " (TL: Top-Left, BR: Bottom-Right, ...)" + ConsoleColors.RESET + "): ");
                String input = scanner.nextLine();
                if (isInputOK(input)) {
                    placeDisc(input);
                }
            }

        board.draw(GUI);
        status();   // Display who won and who lost
    }


    /**
     * Primary method, checks user inputs, then processes the data and returns gma status
     * @param input
     */
    public void placeDisc(String input) {

        // Conventions...
        String[] inputs = input.split(" ");
        int y = Integer.parseInt(inputs[0]) - 1;
        int x = Integer.parseInt(inputs[1]) - 1;
        String quadrant = inputs[2];
        String rotationMode = inputs[3];

        // Set direction
        boolean clockwise;
        if (rotationMode.equals("false"))
            clockwise = false;
        else
            clockwise = true;

        // Place disc
        if (turn % 2 == 0)
            board.setCell(x, y, player1.getCell().getColor());
        else
            board.setCell(x, y, player2.getCell().getColor());

        // First check
        status();

        if (turn != -1) {
            board.rotate(quadrant, clockwise);

            // Second check
            status();

            // Display map
            display(GUI);

            if (turn != -1)
                turn++; // Move on to opponent
        }
    }

    /**
     * See how the game is going, are we done or not?
     */
    public void status() {
        String gameStatus = checkGame();
        if (!gameStatus.equals("0")) {
            if (gameStatus.equals("1"))
                System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "WINNER: " + player1.getPlayerName() + ConsoleColors.RESET);
            else if (gameStatus.equals("2"))
                System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "WINNER: " + player2.getPlayerName() + ConsoleColors.RESET);
            else if (gameStatus.equals("1,2"))
                System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "DRAW: " + player1.getPlayerName() + ", " + player2.getPlayerName() + ConsoleColors.RESET);
            else
                System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "No WINNER" + ConsoleColors.RESET);
            turn = -1;
        }
    }

    /**
     * Getter
     * @return turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     *  This functions handles most of the heavy work to compute game status and report overall score of each player
     * @return
     */
    private String checkGame() {
        Cell[][] map = board.getFullBoard();

        int player1MaxCombo = 0;
        int player2MaxCombo = 0;
        int freeCells = 0;

        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++) {
                if (map[i][j].getColor().equals(player1.getCell().getColor())) {
                    int temp = getMaxCombo(map, i, j, player1);
                    if (temp > player1MaxCombo)
                        player1MaxCombo = temp;
                } else if (map[i][j].getColor().equals(player2.getCell().getColor())) {
                    int temp = getMaxCombo(map, i, j, player2);
                    if (temp > player2MaxCombo)
                        player2MaxCombo = temp;
                } else
                    freeCells++;
            }

        // All possible scenarios
        if (player1MaxCombo == 5 && player2MaxCombo != 5)
            return "1";
        else if (player2MaxCombo == 5 && player1MaxCombo != 5)
            return "2";
        else if (player1MaxCombo == 5 && player2MaxCombo == 5)
            return "1,2";
        else if (freeCells != 0)
            return "0";
        else
            return "-1";
    }

    /**
     * Brute force each direction (NOT OPTIMIZED!!!)
     * @param map map
     * @param i X-Axis
     * @param j Y-Axis
     * @param player Player Obj
     * @return max combo (Current combo)
     */
    private int getMaxCombo(Cell[][] map, int i, int j, Player player) {

        int max = 0;

        // Right
        int sum = 0;
        for (int t = i, k = j; k < map.length; k++)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Left
        sum = 0;
        for (int t = i, k = j; k >= 0; k--)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Up
        sum = 0;
        for (int t = i, k = j; t >= 0; t--)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Down
        sum = 0;
        for (int t = i, k = j; t < map.length; t++)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Up-Left
        sum = 0;
        for (int t = i, k = j; t >= 0 && k >= 0; t--, k--)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Up-Right
        sum = 0;
        for (int t = i, k = j; t >= 0 && k < map.length; t--, k++)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Down-Left
        sum = 0;
        for (int t = i, k = j; t < map.length && k >= 0; t++, k--)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        // Down-Right
        sum = 0;
        for (int t = i, k = j; t < map.length && k < map.length; t++, k++)
            if (player.getCell().getColor().equals(map[t][k].getColor()))
                sum++;
            else
                break;
        if (sum > max)
            max = sum;

        return max;
    }

    /**
     * Checks if user has read the manual and typed the stuff human-like or not (:
     * @param input input string
     * @return
     */
    private boolean isInputOK(String input) {
        try {
            String[] inputs = input.split(" ");
            int y = Integer.parseInt(inputs[0]) - 1;
            int x = Integer.parseInt(inputs[1]) - 1;
            String quadrant = inputs[2];
            String rotationMode = inputs[3];

            Cell cell;
            if (x >= 0 && x < 6 && y >= 0 && y < 6 && (cell = board.getCell(x, y)) != null && (rotationMode.equals("false") || rotationMode.equals("true")) && (quadrant.equals("TL") || quadrant.equals("TR") || quadrant.equals("BL") || quadrant.equals("BR")))
                if (cell.getColor() == ConsoleColors.BLACK)
                    return true;
                else {
                    System.out.println(ConsoleColors.RED_BRIGHT + "\'AlreadyOccupied exception\' thrown by GameExceptionHandler" + ConsoleColors.RESET);
                    return false;
                }

            else {
                System.out.println(ConsoleColors.RED_BRIGHT + "\'IncorrectInputFormat/OutOfBounds exception\' thrown by GameExceptionHandler (Typing mistake?!)" + ConsoleColors.RESET);
                return false;
            }

        } catch (Exception ex) {
            System.out.println(ConsoleColors.RED_BRIGHT + "\'IncorrectInputFormat/InvalidInput exception\' thrown by GameExceptionHandler (See Rules for more information)" + ConsoleColors.RESET);
            return false;
        }
    }

    /**
     * Returns the name of the player that currently has the turn
     * @return
     */
    public String getPlayerTurnName() {
        if (turn % 2 == 0)
            return player1.getPlayerName() + "(" + player1.getCell().getColor() + player1.getCell().getValue() + ConsoleColors.RESET + ")";
        else
            return player2.getPlayerName() + "(" + player2.getCell().getColor() + player2.getCell().getValue() + ConsoleColors.RESET + ")";
    }
}