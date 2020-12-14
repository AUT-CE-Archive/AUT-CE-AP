import java.util.ArrayList;

/**
 * Player Obj contains information about each player such as player's name and score
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Player {

    /**
     * Player name
     */
    private String playerName;
    /**
     * Player score
     */
    private int playerScore;
    /**
     * Player cards
     */
    private ArrayList<Card> playerCards;
    /**
     * Indicates whether player is AI or user
     */
    private boolean isAI;
    /**
     * Players last card
     */
    private Card lastCard;

    /**
     * Player Constructor
     *
     * @param playerName player name
     * @param isAI Does what it says
     */
    public Player(String playerName, boolean isAI) {
        // Initialize fields
        this.playerName = playerName;
        this.playerScore = 0;
        this.isAI = isAI;
        this.playerCards = new ArrayList<>();
        this.lastCard = null;
    }

    /**
     * Increase score
     * @param score Score
     */
    public void increaseScore(int score) {
        this.playerScore += score;
    }

    /**
     * Getter: Get player name
     *
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter: Get player score
     *
     * @return player score
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Getter: Get player's cards list
     * @return  Player's card
     */
    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    /**
     * Removes the card from the list
     *
     * @param cardType card type
     */
    public void removeCard(CardType cardType) {
        for (Card card : playerCards)
            if (card.getCardType() == cardType) {
                playerCards.remove(card);

                // Set as last Card
                lastCard = card;

                break;
            }
    }

    /**
     * Removes the card from the list
     *
     * @param cardName card type
     */
    public void removeCard(String cardName) {
        for (Card card : playerCards)
            if (card.getCardType().name().equals(cardName)) {
                playerCards.remove(card);

                // Set as last Card
                lastCard = card;

                break;
            }
    }

    /**
     * This methods checks whether the requested card exists in the list or not
     *
     * @param cardToCheck card to check
     * @return does the card exist?
     */
    private boolean checkExist(Card cardToCheck) {
        for (Card card : playerCards)
            if (card == cardToCheck)
                return true;

        return false;
    }

    /**
     * This methods checks whether the requested card exists in the list or not
     *
     * @param cardName card type
     * @return does the card exist?
     */
    public boolean checkExist(String cardName) {
        for (Card card : playerCards)
            if (card.getCardType().name().equals(cardName))
                return true;

        return false;
    }

    /**
     * This methods returns the actual card object using its name
     *
     * @param cardName card type
     * @return the card
     */
    public Card getCardByName(String cardName) {
        for (Card card : playerCards)
            if (card.getCardType().name().equals(cardName))
                return card;
        return null;
    }

    /**
     * Adds a card to players cards
     *
     * @param card card
     */
    public void addCard(Card card) {
        playerCards.add(card);
    }

    /**
     * Getter: Get player status
     *
     * @return player status
     */
    public String getPlayerController() {
        if (isAI) return "AI";
        else return "User";
    }

    /**
     * Getter: Get last card
     *
     * @return last card
     */
    public Card getLastCard() {
        return lastCard;
    }

    /**
     * Checks whether player has any ordinary or action cards
     *
     * @param cardOnDeck Card on deck
     * @return indicator
     */
    public boolean hasOrdinaryOrActionCards(Card cardOnDeck) {
        for (Card card : playerCards)
            if (card.getCardColor().equals(cardOnDeck.getCardColor()) || card.getCardValue().equals(cardOnDeck.getCardValue()))
                return true;
        return false;
    }

    /**
     * Checks whether plaeyr has any wild cards or not
     *
     * @return indicator
     */
    public boolean hasWildCards() {
        for (Card card : playerCards)
            if (card.getCardType() == CardType.WILD || card.getCardType() == CardType.WILD_DRAW4)
                return true;
        return false;
    }

    /**
     * This method is the AI part which thinks of the bes possible move
     * _Simple algorithm based on classification and priority selection_
     *
     * @param cardOnDeck card currently on deck
     * @return Selected card
     */
    public Card decide(Card cardOnDeck, Player[] players, int turn, int increment, String color) {

        // Search through every card
        if (color.equals("")) {
            for (Card card : playerCards)
                if (card.getCardValue().equals(cardOnDeck.getCardValue()) || card.getCardColor().equals(cardOnDeck.getCardColor()) || card.getCardType() == CardType.WILD) // Ordinary/Action cards/WILD have are searched through
                    return card;
        } else {
            for (Card card : playerCards)
                if (card.getCardColor().equals(color) || card.getCardType() == CardType.WILD || card.getCardType() == CardType.WILD_DRAW4)  // Wild cards have been used, force color
                    return card;
        }

        // If no other card available
        for (Card card : playerCards)
            if (card.getCardType() == CardType.WILD_DRAW4)
                return card;

        return null;
    }

    /**
     * Check if certain color exists or not
     * @param color color
     * @return indicator
     */
    public boolean checkColor(String color) {
        for (Card card : playerCards)
            if (card.getCardColor().equals(color))  // Wild cards have been used, force color
                return true;
        return false;
    }
}