/**
 * UNO Obj, basically the entire game-engine is here!
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
import java.util.Random;
import java.util.Scanner;

public class UNO {

    /**
     * Players array
     */
    private Player[] players;
    /**
     * Card set Object
     */
    private CardSet cardSet;
    /**
     * Board Object
     */
    private Board board;
    /**
     * whose turn is it?
     */
    private int turn;
    /**
     * Rotation direction
     */
    private int increment;
    /**
     * Java.Util.Random
     */
    private Random random;
    /**
     * Java.Util.Scanner
     */
    Scanner scanner;
    /**
     * Does what it says
     */
    private Card cardOnDeck;
    /**
     * indicates whether action card's effect is active or not
     */
    boolean effect = false;
    /**
     * Wild card color indicator
     */
    private String color;
    /**
     * GUI Indicator
     */
    private boolean GUI;
    /**
     * Players count
     */
    private int playerCount;
    /**
     * AI count
     */
    private int AICount;

    /**
     * UNO Constructor, Initializes every single field
     * @param playerCount Players count (Default is 4, NOT Changeable!!!)
     * @param playerNames Players name array
     */
    public UNO(int playerCount, int AICount, String[] playerNames, boolean GUI) {
        // Initialize fields
        players = new Player[playerCount];
        board = new Board();
        cardSet = new CardSet();
        random = new Random();
        color = "";
        this.GUI = GUI;
        this.playerCount = playerCount;
        this.AICount = AICount;

        // A little trick
        while (true) {
            cardOnDeck = cardSet.pickCard();
            if (cardOnDeck.getCardValue().equals("SKIP") || cardOnDeck.getCardValue().equals("REVERSE") || cardOnDeck.getCardValue().equals("DRAW2") || cardOnDeck.getCardValue().equals("WILD") || cardOnDeck.getCardValue().equals("WILD_DRAW4")) {
                cardSet.addCard(cardOnDeck);
                cardOnDeck = cardSet.pickCard();
            } else
                break;
        }
        scanner = new Scanner(System.in);

        // Choose a player to begin randomly
        turn = random.nextInt(playerCount);

        // Direction (Clock Wise, Counter Clock Wise)
        if (random.nextInt(2) == 0)
            increment = -1;
        else
            increment = +1;

        // Initialize players and give each one 7 cards
        initializePlayers(playerNames);
    }

    /**
     * Initialize players, give each one 7 random cards
     * @param playerNames player names
     */
    private void initializePlayers(String[] playerNames) {
        for (int i = 0; i < players.length; i++) {

            // Initialize Player (1 User, reset are AI)
            if (i < playerCount - AICount)
                players[i] = new Player(playerNames[i], false);
            else
                players[i] = new Player(playerNames[i], true);

            // Give 7 random cards to the player
            for (int j = 0; j < 7; j++)
                players[i].addCard(cardSet.pickCard());
        }
    }

    /**
     * Main method of the entire class
     * @throws InterruptedException
     */
    public void play() throws InterruptedException {

        display("");
        while (!checkGameOver()) {

            checkCurrentMovesAndModify();
            reCheckCurrentMovesAndModify();

            if (cardOnDeck.getCardValue().equals("SKIP") && effect) {   // Skip
                increaseTurn();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn].getPlayerController() + ") has been skipped." + ConsoleColors.RESET);
                increaseTurn();
                Thread.sleep(1000);
                effect = false;
            } else if (cardOnDeck.getCardValue().equals("REVERSE") && effect) {   // Reverse
                if (playerCount != 2) {
                    increment *= -1;
                    increaseTurn();
                }
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Rotation has been reversed." + ConsoleColors.RESET);
                Thread.sleep(1000);
                effect = false;
            } else if (cardOnDeck.getCardValue().equals("DRAW2") && effect) {   // Draw2
                increaseTurn();
                String cards = "";
                for (int i = 0; i < 2; i++) {
                    Card card = cardSet.pickCard();
                    players[turn].addCard(card);
                    cards += board.printCard(card, ", ");
                }
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn].getPlayerController() + ") has been given 2 cards and is skipped " + ConsoleColors.RESET + "(" + cards + ")");
                Thread.sleep(1000);
                increaseTurn(); // Skip the player
                effect = false;
            } else if ((cardOnDeck.getCardValue().equals("DRAW4") || cardOnDeck.getCardValue().equals("WILD")) && effect) {   // Wild cards
                increaseTurn();
                if (cardOnDeck.getCardValue().equals("DRAW4")) {
                    String cards = "";
                    for (int i = 0; i < 4; i++) {
                        Card card = cardSet.pickCard();
                        players[turn].addCard(card);
                        cards += board.printCard(card, ", ");
                    }
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn].getPlayerController() + ") has been given 4 cards and is skipped " + ConsoleColors.RESET + "(" + cards + ")");

                    increaseTurn();
                }
                switch (random.nextInt(4)) {
                    case 0:
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Change color to: Yellow" + ConsoleColors.RESET);
                        color = ConsoleColors.YELLOW_BACKGROUND_BRIGHT;
                        break;
                    case 1:
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Change color to: Red" + ConsoleColors.RESET);
                        color = ConsoleColors.RED_BACKGROUND_BRIGHT;
                        break;
                    case 2:
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Change color to: Green" + ConsoleColors.RESET);
                        color = ConsoleColors.GREEN_BACKGROUND_BRIGHT;
                        break;
                    case 3:
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Change color to: Blue" + ConsoleColors.RESET);
                        color = ConsoleColors.BLUE_BACKGROUND_BRIGHT;
                        break;
                }

                Thread.sleep(1000);
                effect = false;
            } else
                increaseTurn();
        }

        winner();
    }

    /**
     * Check if user/AI has any moves with current cards
     */
    public void checkCurrentMovesAndModify() {

        if (!color.equals("") && !players[turn].checkColor(color) && !players[turn].hasWildCards()) {
            Card temp = cardSet.pickCard();
            players[turn].addCard(temp);

            // Display what has changed
            display(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn] .getPlayerController() + ") has been given a card, because it didn't have any moves (" + board.printCard(temp, "") + ConsoleColors.RESET + ")");

        } else if (color.equals("") && !players[turn].hasOrdinaryOrActionCards(cardOnDeck) && !players[turn].hasWildCards()) {

            Card temp = cardSet.pickCard();
            players[turn].addCard(temp);

            // Display what has changed
            display(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn].getPlayerController() + ") has been given a card, because it didn't have any moves (" + board.printCard(temp, "") + ConsoleColors.RESET + ")");
        }
    }

    /**
     * Re-checks, If user still doesn't have any moves, then moves on to the next player
     */
    public void reCheckCurrentMovesAndModify() {

        if (players[turn].hasOrdinaryOrActionCards(cardOnDeck) || players[turn].hasWildCards() || (!color.equals("") && players[turn].checkColor(color))) {

            String input;
            if (players[turn].getPlayerController().equals("User"))
                input = getUserInput(players[turn]);
            else {
                Card selection = players[turn].decide(cardOnDeck, players, turn, increment, color);
                color = ""; // Reset Wild color
                input = selection.getCardType().name();
            }

            cardSet.addCard(cardOnDeck);    // Return the previous card to the set
            cardOnDeck = players[turn].getCardByName(input);    // First, place the card on deck
            players[turn].removeCard(input);    // Third, remove the card

            if (cardOnDeck.getCardValue().equals("SKIP") || cardOnDeck.getCardValue().equals("REVERSE") || cardOnDeck.getCardValue().equals("DRAW2") || cardOnDeck.getCardValue().equals("WILD") || cardOnDeck.getCardValue().equals("DRAW4"))
                effect = true;

            // Display what has changed
            display(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn].getPlayerController() + ") has made a move (" + board.printCard(cardOnDeck, "") + ConsoleColors.RESET + ")");
        } else
            display(ConsoleColors.BLUE_BOLD_BRIGHT + players[turn].getPlayerName() + "(" + players[turn].getPlayerController() + ") has been passed, because It still didn't have any moves." + ConsoleColors.RESET);
    }

    /**
     * Prints the winner and the score board
     */
    private void winner() {
        // Print the winner
        for (Player player: players)
            if (player.getPlayerCards().size() == 0)
                System.out.println(ConsoleColors.GREEN_BOLD_BRIGHT + "WINNER: " + player.getPlayerName() + ConsoleColors.RESET);
        int[] scores = new int[players.length];
        for (int i = 0; i < scores.length; i++) {
            int sum = 0;
            for (Card card: players[i].getPlayerCards())
                sum += card.getCardScore();
            scores[i] = sum;
        }

        // Print the score board
        System.out.println("\n- " + ConsoleColors.GREEN_BOLD_BRIGHT + "Score Board: ");
        for (int i = 0; i < scores.length; i++) {
            System.out.print(players[i].getPlayerName() + ": " + scores[i]);
            if (scores[i] == 0)
                System.out.print("\tWINNER!!!\n");
            else
                System.out.print("\n");
        }
        System.out.print(ConsoleColors.RESET);
    }

    /**
     * This method gets user inputs and handles any dumbness of users inputs (:
     * @param player Current User Obj
     * @return User Input
     */
    private String getUserInput(Player player) {

        String input;
        while (true) {
            System.out.print(player.getPlayerName() + "'s Turn) Enter your card (i.e. RED_7, GREEN_SKIP, WILD, WILD_DRAW4, ...): ");
            input = scanner.nextLine();

            if (color.split("_")[0].equals(input.split("_")[0]))
                break;

            if (!player.checkExist(input))
                System.out.println(ConsoleColors.RED_BRIGHT + "\'CardNotFound exception\' thrown by Game_ExceptionHandler (Mention both color and the name)" + ConsoleColors.RESET);
            else if (!(player.getCardByName(input).getCardValue().equals(cardOnDeck.getCardValue()) || player.getCardByName(input).getCardColor().equals(cardOnDeck.getCardColor()))) {
                if (player.hasWildCards())
                    break;
                else
                    System.out.println(ConsoleColors.RED_BRIGHT + "\'InvalidMove\' thrown by Game_ExceptionHandler (See Rules)" + ConsoleColors.RESET);
            } else
                break;
        }

        return input;
    }

    /**
     * Checks whether we have a winner or not
     * @return indicator of game status
     */
    public boolean checkGameOver() {
        for(Player player: players)
            if (player.getPlayerCards().size() == 0)
                return true;
        return false;
    }

    /**
     * Does what it says
     * @param action last player's action string
     */
    public void display(String action) { board.draw(players, getPlayerTurn(), getDirection(), cardOnDeck, action, GUI, getNextPlayer()); }

    /**
     * Increment, handles both directions
     */
    private void increaseTurn() {
        turn += increment;
        if (turn > playerCount - 1)
            turn = 0;
        if (turn < 0)
            turn = playerCount - 1;
    }

    /**
     * Returns the previous player's turn
     * @return previous player's turn
     */
    private int getPreviousPlayer() {
        int previousTurn = turn - increment;
        if (previousTurn > playerCount - 1)
            previousTurn = 0;

        if (previousTurn < 0)
            previousTurn = playerCount - 1;
        return previousTurn;
    }

    /**
     * Returns next player's turn
     * @return previous player's turn
     */
    private int getNextPlayer() {
        int nextTurn = turn + increment;
        if (nextTurn > playerCount - 1)
            nextTurn = 0;

        if (nextTurn < 0)
            nextTurn = playerCount - 1;
        return nextTurn;
    }

    /**
     * Getter: Get whose turn it is
     * @return string
     */
    private String getPlayerTurn() {
        return players[turn].getPlayerName();
    }

    /**
     * Getter: Get direction string
     * @return direction string
     */
    private String getDirection() {
        if (increment == 1)
            return "Clock Wise";
        else
            return "Counter Clock Wise";
    }
}