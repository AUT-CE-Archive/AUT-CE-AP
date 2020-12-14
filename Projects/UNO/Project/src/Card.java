/**
 * Card Obj, contains information about each cards color, value, type, ... and contains methods to process the given card and break into its data
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Card {

    /**
     * Card type (Enum)
     */
    private CardType cardType;
    /**
     * Card color
     */
    private String cardColor;
    /**
     * Card score (Initialized 0)
     */
    private int cardScore;
    /**
     * Card value
     */
    private String cardValue;

    /**
     * #1 Card Constructor
     * Initializes the card information
     * @param cardType card type
     */
    public Card(CardType cardType) { initialize(cardType); }

    /**
     * #2 Card Constructor (@override)
     * Searches for the specific card, if found, initializes the card information
     * @param cardColor card color
     * @param cardValue card value
     */
    public Card(String cardColor, String cardValue) {
        for (CardType type : CardType.values())
            if (type.name().equals(cardColor + "_" + cardValue)) {
                initialize(type);
                break;
            }
    }

    /**
     * This method, processes the information corresponding to the card type
     * @param cardType card type
     */
    private void initialize(CardType cardType) {

        this.cardType = cardType;
        String cardType_string = cardType.toString();

        if (cardType == CardType.WILD || cardType == CardType.WILD_DRAW4) { // Wild cards
            cardScore = 50;
            cardColor = "WILD";
            if (cardType == CardType.WILD)
                cardValue = "WILD";
            else
                cardValue = "DRAW4";
        } else if (cardType_string.contains("_SKIP") || cardType_string.contains("_REVERSE") || cardType_string.contains("_DRAW2")) { // Action cards
            cardScore = 20;
            cardColor = cardType_string.split("_")[0];
            cardValue = cardType_string.split("_")[1];
        } else {    // Ordinary cards
            cardScore = Integer.parseInt(cardType_string.split("_")[1]);
            cardColor = cardType_string.split("_")[0];
            cardValue = cardType_string.split("_")[1];
        }

        // Adjust the card color according to the available Console Color
        adjustConsoleColor();
    }

    /**
     * Getter: Get card type
     * @return card type
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Getter: Get card score
     * @return Card score
     */
    public int getCardScore() {
        return cardScore;
    }

    /**
     * Getter: Get card color
     * @return Card color
     */
    public String getCardColor() {
        return cardColor;
    }

    /**
     * Getter: Get card value
     * @return Card value
     */
    public String getCardValue() { return cardValue; }

    /**
     * Adjust the card color according to the available Console Color
     */
    private void adjustConsoleColor() {
        switch (this.cardColor) {
            case "RED": this.cardColor = ConsoleColors.RED_BACKGROUND_BRIGHT; break;
            case "BLUE": this.cardColor = ConsoleColors.BLUE_BACKGROUND_BRIGHT; break;
            case "GREEN": this.cardColor = ConsoleColors.GREEN_BACKGROUND_BRIGHT; break;
            case "YELLOW": this.cardColor = ConsoleColors.YELLOW_BACKGROUND_BRIGHT; break;
            default: this.cardColor = ConsoleColors.BLACK_BACKGROUND_BRIGHT; break;
        }
    }
}