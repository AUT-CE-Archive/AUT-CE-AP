/**
 * Quadrant Object, contains one quarter of the map with various methods to manipulate its data
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Quadrant {

    /**
     * Cellar map
     */
    private Cell[][] map;

    /**
     * Obj Constructor
     * @param length dimension
     */
    public Quadrant(int length) {
        map = new Cell[length][length];

        // Initialize Board
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++)
                map[i][j] = new Cell(ConsoleColors.BLACK);
    }

    /**
     * Rotate method (90/-90 Degrees turn)
     * @param clockWise clock wise or counter clock wise
     */
    public void transpose(boolean clockWise) {

        Cell[][] tempMap = new Cell[map.length][map.length];
        if (clockWise) {    // 90 Deg

            int k = 2;
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map.length; j++)
                    tempMap[j][k] = map[i][j];
                k--;
            }

        } else {    // -90 Deg

            for (int i = 0; i < map.length; i++) {
                int k = 2;
                for (int j = 0; j < map.length; j++) {
                    tempMap[k][i] = map[i][j];
                    k--;
                }
            }
        }

        // Replace maps
        map = tempMap;
    }

    /**
     * Change cell DNA
     * @param i X-Axis
     * @param j Y-Axis
     * @param color color
     */
    public void changeCell(int i, int j, String color) {
        map[i][j].setColor(color);
    }

    /**
     * Getter
     * @return size
     */
    public int getSize() {
        return map.length;
    }

    /**
     * Getter
     * @return cellar map
     */
    public Cell[][] getMap() {
        return map;
    }
}
