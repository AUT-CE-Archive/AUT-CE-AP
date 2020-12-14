/**
 * Cell Object contains information about value, color, type and coordinates of the cell along with methods to manipulate these data.
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Cell {

    /**
     * Value
     */
    String value;
    /**
     * Color
     */
    String color;

    /**
     * Obj Constructor
     * @param color Color
     */
    public Cell(String color) {
        this.value = "●";
        this.color = color;
    }

    /**
     * Setter
     * @param color color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Getter
     * @return color
     */
    public String getColor() {
        return color;
    }

    /**
     * Getter
     * @return value
     */
    public String getValue() {
        return value;
    }
}
