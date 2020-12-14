/**
 * Cell Object contains information about value, color, type and coordinates of the cell along with methods to manipulate these data.
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
class Cell {

    /**
     * Cell value
     */
    private String value;
    /**
     * Cell type (WHITE, BLACK, OPTION, EMPTY)
     */
    private CellType type;
    /**
     * Cell color corresponding to its type
     */
    private String color;
    /**
     * Cell coordinates in Board
     */
    public int i, j;

    /**
     * Cell Constructor
     *
     * @param type Cell type
     * @param i    Cell X-Axis
     * @param j    Cell Y-Axis
     */
    public Cell(CellType type, int i, int j) {
        // Initialize Obj fields
        this.type = type;
        this.i = i;
        this.j = j;

        // Set corresponding color and value using Cell type
        setType(type);
    }

    /**
     * Set type, value and its corresponding visual color
     *
     * @param type Cell type
     */
    public void setType(CellType type) {
        this.type = type;

        // Set value
        if (type == CellType.EMPTY)
            this.value = " ";
        else
            this.value = "●";

        // Set visual color
        switch (type) {
            case WHITE:
                this.color = ConsoleColors.WHITE;
                break;
            case BLACK:
                this.color = ConsoleColors.BLACK_BOLD_BRIGHT;
                break;
            case EMPTY:
                this.color = ConsoleColors.RESET;
                break;
            case OPTION:
                this.color = ConsoleColors.YELLOW_BRIGHT;
                break;
        }
    }

    /**
     * Getter: Cell type
     *
     * @return CellType
     */
    public CellType getType() {
        return this.type;
    }

    /**
     * Getter: Cell color
     *
     * @return color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Getter: Cell value
     *
     * @return value
     */
    public String getValue() {
        return value;
    }
}