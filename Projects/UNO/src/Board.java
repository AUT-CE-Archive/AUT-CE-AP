/**
 * Board contains methods for drawing the visual board with all the colors and stuff
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Board {

    /**
     * A bunch of none-sense brute-force algorithms which I have no fucking Idea how got into my head, but it works OMG!! (;
     *
     * @param players Players array
     * @param playerTurn Players turn name
     * @param direction Rotation direction
     * @param cardOnDeck Card on deck
     * @param action Action string
     */
    public void draw(Player[] players, String playerTurn, String direction, Card cardOnDeck, String action, boolean GUI, int nextPlayerTurn) {

        if (GUI) {
            String visualMap = "";

            visualMap += ("\n\n\nTurn: " + playerTurn + "\t-\tNext player: " + players[nextPlayerTurn].getPlayerName() + "\t-\tDirection: " + direction + "\t-\tCard On Deck: " + printCard(cardOnDeck, "") + "\n");
            // Top Border
            for (int i = 0; i < 30; i++)
                visualMap += " ─── ";
            visualMap += "\n";

            // Player2 Info
            for (int i = 0; i < 75 - ((players[1].getPlayerName().length() + 3 + players[1].getPlayerController().length()) / 2); i++)
                visualMap += " ";
            visualMap += (players[1].getPlayerName() + " (" + players[1].getPlayerController() + ")\n");
            String cardSet = drawCard(players[1], " ");

            int length = 0;
            for (Card card : players[1].getPlayerCards())
                length += (card.getCardValue().length() + 3);
            for (int i = 0; i < 75 - (length - 1) / 2; i++)
                visualMap += " ";
            visualMap += cardSet + "\n\n";

            // Player1 & 3 Info
            int len1 = players[0].getPlayerCards().size();
            int len2 = players[2].getPlayerCards().size();
            int max = Math.max(len1, len2);
            int min = Math.min(len1, len2);
            if (min == 0)
                min = 1;

            // Name manipulation
            String name1 = players[0].getPlayerName();
            String name2 = players[2].getPlayerName();
            if (name1.length() > 7)
                name1 = name1.substring(0, 5) + "..";
            if (name2.length() > 7)
                name2 = name2.substring(0, 5) + "..";

            for (int i = 0; i < max; i++) {
                int length1 = name1.length();
                int length2 = name2.length();

                // Player1 name
                if (i == min - 1)
                    visualMap += (name1 + " ");
                else
                    for (int j = 0; j < name1.length() + 1; j++)
                        visualMap += " ";

                // Player1 cards
                try {
                    Card card = players[0].getPlayerCards().get(i);

                    visualMap += printCard(card, "");

                    length1 += card.getCardValue().length() + 2;
                } catch (Exception ignored) {
                }

                if (i + 1 > players[2].getPlayerCards().size())
                    visualMap += "\n";

                // Player3
                try {
                    Card card = players[2].getPlayerCards().get(i);
                    length2 += card.getCardValue().length() + 2;

                    if (i == min - 1) {
                        for (int j = 0; j < (150 - players[1].getPlayerName().length() - cardOnDeck.getCardValue().length() - 2 - players[0].getPlayerCards().get(i).getCardValue().length() - 2 - 1 - length1) / 2; j++)
                            visualMap += " ";
                        visualMap += printCard(cardOnDeck, "");
                        for (int j = 0; j < 150 - ((150 - players[1].getPlayerName().length() - cardOnDeck.getCardValue().length() - 2 - players[0].getPlayerCards().get(i).getCardValue().length() - 2 - 1 - length1) / 2) - length2 - cardOnDeck.getCardValue().length() - length1 - 2; j++)
                            visualMap += " ";
                    } else {
                        for (int j = 0; j < 150 - length1 - length2; j++)
                            visualMap += " ";
                    }

                    visualMap += printCard(card, "");

                    // Player3 name
                    if (i == min - 1)
                        visualMap += (" " + name2);
                    else
                        for (int j = 0; j < name2.length() + 1; j++)
                            visualMap += " ";
                    visualMap += "\n";

                } catch (Exception ignored) {
                }
            }

            // Player4 Info
            visualMap += "\n";
            cardSet = drawCard(players[3], " ");
            length = 0;
            for (Card card : players[3].getPlayerCards())
                length += (card.getCardValue().length() + 3);
            for (int i = 0; i < 75 - (length - 1) / 2; i++)
                visualMap += " ";
            visualMap += (cardSet + "\n");

            for (int i = 0; i < 75 - ((players[3].getPlayerName().length() + 3 + players[3].getPlayerController().length()) / 2); i++)
                visualMap += " ";
            visualMap += (players[3].getPlayerName() + " (" + players[3].getPlayerController() + ")\n");


            // Bottom border
            for (int i = 0; i < 30; i++)
                visualMap += " ─── ";
            visualMap += "\n";

            // Print last card for each player
            visualMap += "Last Cards)\t  ";
            for (int i = 0; i < players.length; i++)
                if (i != players.length - 1)
                    visualMap += players[i].getPlayerName() + ": " + printCard(players[i].getLastCard(), "") + " - ";
                else
                    visualMap += players[i].getPlayerName() + ": " + printCard(players[i].getLastCard(), "");

            // Print the entire visual map
            System.out.println(visualMap + "\n" + action);
        } else {
            String visualMap = "";
            // Top info
            visualMap += ("\n\n\nTurn: " + playerTurn + "\t-\tDirection: " + direction + "\t-\tNext Player: " + players[nextPlayerTurn].getPlayerName() + "\t-\tCard On Deck: " + printCard(cardOnDeck, "") + "\n");
            // Players
            for (Player player: players)
                visualMap += player.getPlayerName() + "(" + player.getPlayerController() + "): " + drawCard(player, " ") + "\n";

            // Print last card for each player
            visualMap += "Last Cards)\t  ";
            for (int i = 0; i < players.length; i++)
                if (i != players.length - 1)
                    visualMap += players[i].getPlayerName() + ": " + printCard(players[i].getLastCard(), "") + " - ";
                else
                    visualMap += players[i].getPlayerName() + ": " + printCard(players[i].getLastCard(), "");

            System.out.println(visualMap + "\n" + action + "\n");
        }
    }

    /**
     * Draws a set of cards for the player
     * @param player Player
     * @param separator Separator string
     * @return Cards visual column string
     */
    private String drawCard(Player player, String separator) {
        String cardSet = "";
        for (Card card : player.getPlayerCards()) {
            cardSet += printCard(card, separator);
        }
        return cardSet;
    }

    /**
     * Print a single card
     * @param card Card
     * @param separator Separator string
     * @return does what it says
     */
    public String printCard(Card card, String separator) {
        if (card != null)
            if (!card.getCardColor().equals(ConsoleColors.YELLOW_BACKGROUND_BRIGHT))
                return (card.getCardColor() + ConsoleColors.WHITE_BOLD_BRIGHT + " " + card.getCardValue() + " " + ConsoleColors.RESET + separator);
            else
                return (card.getCardColor() + ConsoleColors.BLACK_BOLD_BRIGHT + " " + card.getCardValue() + " " + ConsoleColors.RESET + separator);
        else
            return "NaN";
    }
}