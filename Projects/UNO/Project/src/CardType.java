/**
 * Enumeration for different card types in the game
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public enum CardType {
    /**
     * Wild Cards (Wild, Wild Draw 2)
     */
    WILD, WILD_DRAW4,
    /**
     * Action Cards (Yellow, Green, Blue, Red) (Skip, Reverse, Draw 2)
     */
    YELLOW_SKIP, GREEN_SKIP, BLUE_SKIP, RED_SKIP,
    YELLOW_DRAW2, GREEN_DRAW2, BLUE_DRAW2, RED_DRAW2,
    YELLOW_REVERSE, GREEN_REVERSE, BLUE_REVERSE, RED_REVERSE,

    /**
     * Ordinary Cards (Yellow, Green, Blue, Red) (0 - 9)
     */
    YELLOW_0, YELLOW_1, YELLOW_2, YELLOW_3, YELLOW_4, YELLOW_5, YELLOW_6, YELLOW_7, YELLOW_8, YELLOW_9,
    RED_0, RED_1, RED_2, RED_3, RED_4, RED_5, RED_6, RED_7, RED_8, RED_9,
    GREEN_0, GREEN_1, GREEN_2, GREEN_3, GREEN_4, GREEN_5, GREEN_6, GREEN_7, GREEN_8, GREEN_9,
    BLUE_0, BLUE_1, BLUE_2, BLUE_3, BLUE_4, BLUE_5, BLUE_6, BLUE_7, BLUE_8, BLUE_9
}
