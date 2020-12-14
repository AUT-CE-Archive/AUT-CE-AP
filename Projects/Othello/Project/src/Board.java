/**
 * Board Object, contains the data map as well as methods to print the visual map
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Board {

    /**
     * Cell map
     */
    private Cell[][] map;

    /**
     * Board Constructor
     *
     * @param size Size of the map (Default = 8)
     */
    public Board(int size) {

        // Initialize map
        map = new Cell[size][size];
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++)
                map[i][j] = new Cell(CellType.EMPTY, i, j);

        // Initialize each player's cells
        map[size / 2 - 1][size / 2 - 1].setType(CellType.WHITE);
        map[size / 2][size / 2].setType(CellType.WHITE);

        map[size / 2 - 1][size / 2].setType(CellType.BLACK);
        map[size / 2][size / 2 - 1].setType(CellType.BLACK);
    }

    /**
     * Draw method, draws the visual map and both(vertical and horizontal rulers) using the uni-codes and data map
     */
    public void draw(boolean GUI) {

        if (GUI) {
            String[][] visualMap = new String[map.length][map.length];
            String mapString = "";

            mapString += "\n" + ConsoleColors.BLUE_BOLD_BRIGHT + "       A     B     C     D     E     F     G     H   \n   ";
            for (int j = 0; j < visualMap.length * 2 + 1; j++)
                mapString += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET;
            mapString += "\n";

            for (int i = 0; i < visualMap.length; i++) {

                mapString += (" " + ConsoleColors.BLUE_BOLD_BRIGHT + (i + 1) + ConsoleColors.RESET + " ");
                for (int j = 0; j < visualMap.length; j++) {
                    switch (map[i][j].getColor()) {
                        case ConsoleColors.WHITE:
                            visualMap[i][j] = ConsoleColors.RED_BACKGROUND + "   " + ConsoleColors.RESET;
                            break;
                        case ConsoleColors.BLACK_BOLD_BRIGHT:
                            visualMap[i][j] = ConsoleColors.BLUE_BACKGROUND + "   " + ConsoleColors.RESET;
                            break;
                        case ConsoleColors.YELLOW_BRIGHT:
                            visualMap[i][j] = ConsoleColors.YELLOW_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET;
                            break;
                        default:
                            visualMap[i][j] = ConsoleColors.WHITE_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET;
                            break;
                    }
                    mapString += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET;
                    mapString += visualMap[i][j];
                }
                mapString += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET;

                mapString += "\n   ";
                for (int j = 0; j < visualMap.length * 2 + 1; j++)
                    mapString += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET;
                mapString += "\n";
            }
            System.out.println(mapString);
        } else {

            // By converting the algorithm into an string, lagging problems for VS is minimized
            String visualMap = "\n" + ConsoleColors.BLUE_BRIGHT + "    A   B   C   D   E   F   G   H   "
                    + ConsoleColors.RESET + "\n   ─── ─── ─── ─── ─── ─── ─── ─── \n";

            for (int i = 0; i < map.length; i++) {
                visualMap += ConsoleColors.BLUE_BRIGHT + (i + 1) + ConsoleColors.RESET + " ";
                for (int j = 0; j < map.length; j++)
                    visualMap += "| " + map[i][j].getColor() + map[i][j].getValue() + ConsoleColors.RESET + " ";

                visualMap += "|\n   ─── ─── ─── ─── ─── ─── ─── ─── \n";
            }

            System.out.print(visualMap);
        }
    }

    /**
     * Reset Options: Resets all cells that are type-OPTION
     */
    public void resetOptions() {
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map.length; j++)
                if (map[i][j].getType() == CellType.OPTION)
                    map[i][j].setType(CellType.EMPTY);
    }

    /**
     * Getter: Get map
     *
     * @return 2D map
     */
    public Cell[][] getMap() {
        return map;
    }

    /**
     * Modifies the cell using its coordinates on map
     *
     * @param cell Cell
     * @param i X-Axis
     * @param j Y-Axis
     */
    public void changeCell(Cell cell, int i, int j) {
        map[i][j] = cell;
    }

    /**
     * Getter: Get cell
     *
     * @param i X-Axis
     * @param j Y-Axis
     * @return Cell
     */
    public Cell getCell(int i, int j) {
        return map[i][j];
    }
}