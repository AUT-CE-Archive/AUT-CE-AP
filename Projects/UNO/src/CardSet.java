/**
 * CardSet object contains all 108 cards for each set. Also contains methods to handle user requests
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
import java.util.ArrayList;
import java.util.Random;

public class CardSet {

    /**
     * Legendary set (;
     */
    private ArrayList<Card> cards;

    /**
     * CardSet Constructor
     */
    public CardSet() {
        // Initialize ArrayList
        cards = new ArrayList<>();

        // Wild cards
        for (int i = 0; i < 4; i++)
            cards.add(new Card(CardType.WILD));

        // Wild_Draw4 cards
        for (int i = 0; i < 4; i++)
            cards.add(new Card(CardType.WILD_DRAW4));

        // Action cards
        for (int i = 0; i < 2; i++) {
            // Reverse
            cards.add(new Card(CardType.RED_REVERSE));
            cards.add(new Card(CardType.BLUE_REVERSE));
            cards.add(new Card(CardType.YELLOW_REVERSE));
            cards.add(new Card(CardType.GREEN_REVERSE));
            // Skip
            cards.add(new Card(CardType.RED_SKIP));
            cards.add(new Card(CardType.BLUE_SKIP));
            cards.add(new Card(CardType.YELLOW_SKIP));
            cards.add(new Card(CardType.GREEN_SKIP));
            // Draw2
            cards.add(new Card(CardType.RED_DRAW2));
            cards.add(new Card(CardType.BLUE_DRAW2));
            cards.add(new Card(CardType.YELLOW_DRAW2));
            cards.add(new Card(CardType.GREEN_DRAW2));
        }

        // Ordinary cards (1 - 9) (Red, Green, Blue, Yellow)
        String[] colors = new String[]{"RED", "GREEN", "YELLOW", "BLUE"};
        for (int k = 0; k < 2; k++)
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 9; j++) {
                    for (CardType type : CardType.values())
                        if (type.name().equals(colors[i] + "_" + (j + 1)))
                            cards.add(new Card(type));
                }
            }

        // Ordinary cards (zeros)
        cards.add(new Card(CardType.RED_0));
        cards.add(new Card(CardType.GREEN_0));
        cards.add(new Card(CardType.YELLOW_0));
        cards.add(new Card(CardType.BLUE_0));
    }

    /**
     * Getter: Get cards list
     * @return Cards list
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * This methods checks whether the requested card exists in the list or not
     * @param cardType card type
     * @return does the card exist?
     */
    public boolean checkExist(CardType cardType) {
        for (Card card: cards)
            if (card.getCardType() == cardType)
                return true;

        return false;
    }

    /**
     * this method removes card from set using a modern approach (;
     * @param cardType card type
     */
    public void removeCard(CardType cardType) {
        for (Card card: cards)
            if (card.getCardType() == cardType) {
                cards.remove(card);
                break;
            }
    }

    /**
     * Pick a random card, remove it from set; Finally, return it
     * @return Selected Card
     */
    public Card pickCard() {

        // Choose a random card from the set
        Random random = new Random();
        Card selectedCard = cards.get(random.nextInt(cards.size()));

        // Remove from the set
        removeCard(selectedCard.getCardType());

        return selectedCard;
    }

    /**
     * Add card
     * @param card Card
     */
    public void addCard(Card card) {
        cards.add(card);
    }
}
