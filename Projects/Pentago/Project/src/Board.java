/**
 * Board contains four quadrants and methods to manipulate each one
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class Board {

    /**
     * Quedrants
     */
    private Quadrant TL, TR, BL, BR;    // Top-Left, Top-Right, Bottom-Left, Bottom-Right
    /**
     * Divider color
     */
    private String dividerColor;

    /**
     * Obj Constructor
     *
     * @param length size of quadrants
     */
    public Board(int length) {

        // Initialize Quadrants
        TL = new Quadrant(length);
        TR = new Quadrant(length);
        BL = new Quadrant(length);
        BR = new Quadrant(length);

        dividerColor = ConsoleColors.BLUE_BOLD_BRIGHT;
    }

    /**
     * Attaches all four quadrants into one
     *
     * @return full map
     */
    public Cell[][] getFullBoard() {
        Cell[][] map = new Cell[TL.getSize() * 2][TL.getSize() * 2];
        Cell[][] map1 = TL.getMap();
        Cell[][] map2 = TR.getMap();
        Cell[][] map3 = BL.getMap();
        Cell[][] map4 = BR.getMap();

        // Top-Left
        for (int i = 0; i < TL.getSize(); i++)
            for (int j = 0; j < TL.getSize(); j++)
                map[i][j] = map1[i][j];

        // Top-Right
        for (int i = 0; i < TR.getSize(); i++)
            for (int j = 0; j < TR.getSize(); j++)
                map[i][j + 3] = map2[i][j];

        // Bottom-Left
        for (int i = 0; i < BL.getSize(); i++)
            for (int j = 0; j < BL.getSize(); j++)
                map[i + 3][j] = map3[i][j];

        // Bottom-Right
        for (int i = 0; i < BR.getSize(); i++)
            for (int j = 0; j < BR.getSize(); j++)
                map[i + 3][j + 3] = map4[i][j];

        return map;
    }

    /**
     * Get cell based on user input
     *
     * @param i X-Axis
     * @param j Y-Axis
     * @return cell
     */
    public Cell getCell(int i, int j) {
        if (i >= 0 && i < 3 && j >= 0 && j < 3)   // Top-Left
            return TL.getMap()[i][j];
        else if (i >= 0 && i < 3 && j > 2 && j < 6)  // Top-Right
            return TR.getMap()[i][j - 3];
        else if (i > 2 && i < 6 && j >= 0 && j < 3)  // Bottom-Left
            return BL.getMap()[i - 3][j];
        else if (i > 2 & i < 6 && j > 2 && j < 6)   // Bottom-Right
            return BR.getMap()[i - 3][j - 3];
        else
            return null;
    }

    /**
     * Changes cell
     *
     * @param i     X-Axis
     * @param j     Y-Axis
     * @param color color
     */
    public void setCell(int i, int j, String color) {
        if (i >= 0 && i < 3 && j >= 0 && j < 3)   // Top-Left
            TL.changeCell(i, j, color);
        else if (i >= 0 && i < 3 && j > 2 && j < 6)  // Top-Right
            TR.changeCell(i, j - 3, color);
        else if (i > 2 && i < 6 && j >= 0 && j < 3)  // Bottom-Left
            BL.changeCell(i - 3, j, color);
        else if (i > 2 & i < 6 && j > 2 && j < 6)   // Bottom-Right
            BR.changeCell(i - 3, j - 3, color);
    }

    /**
     * rotate quadrant
     *
     * @param quadrant  direction
     * @param clockWise boolean
     */
    public void rotate(String quadrant, Boolean clockWise) {
        if (quadrant.equals("TL"))   // Top-Left
            TL.transpose(clockWise);
        else if (quadrant.equals("TR"))  // Top-Right
            TR.transpose(clockWise);
        else if (quadrant.equals("BL"))  // Bottom-Left
            BL.transpose(clockWise);
        else if (quadrant.equals("BR"))   // Bottom-Right
            BR.transpose(clockWise);
    }

    /**
     * Draw method, nothing especial about it to comment
     */
    public void draw(boolean GUI) {

        String visualMap = "";
        Cell[][] map1 = TL.getMap();
        Cell[][] map2 = TR.getMap();
        Cell[][] map3 = BL.getMap();
        Cell[][] map4 = BR.getMap();
        int length = TL.getSize();

        if (GUI) {

            visualMap += (dividerColor + "\n       1     2     3           4     5     6" + ConsoleColors.RESET + "\n   ");
            for (int j = 0; j < map1.length * 2 + 1; j++)
                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
            visualMap += "   ";
            for (int j = 0; j < map1.length * 2 + 1; j++)
                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
            visualMap += "\n";

            // Draw Top-Left and Top-Right Quadrants
            for (int i = 0; i < length; i++) {

                visualMap += " " + dividerColor + (i + 1) + ConsoleColors.RESET + " ";

                for (int j = 0; j < length; j++)
                    visualMap += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + transformColor(map1[i][j].getColor()) + "   " + ConsoleColors.RESET;

                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "   ");

                for (int j = 0; j < length; j++)
                    visualMap += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + transformColor(map2[i][j].getColor()) + "   " + ConsoleColors.RESET;

                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "\n   ");
                for (int j = 0; j < map1.length * 2 + 1; j++)
                    visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
                visualMap += "   ";
                for (int j = 0; j < map1.length * 2 + 1; j++)
                    visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
                visualMap += "\n";
            }

            visualMap += "\n   ";
            for (int j = 0; j < map1.length * 2 + 1; j++)
                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
            visualMap += "   ";
            for (int j = 0; j < map1.length * 2 + 1; j++)
                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
            visualMap += "\n";

            // Draw Bottom-Left and Bottom-Right Quadrants
            for (int i = 0; i < length; i++) {

                visualMap += " " + dividerColor + (i + 4) + ConsoleColors.RESET + " ";
                for (int j = 0; j < length; j++)
                    visualMap += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + transformColor(map3[i][j].getColor()) + "   " + ConsoleColors.RESET;

                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "   ");

                for (int j = 0; j < length; j++)
                    visualMap += ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + transformColor(map4[i][j].getColor()) + "   " + ConsoleColors.RESET;

                visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET + "\n   ");
                for (int j = 0; j < map1.length * 2 + 1; j++)
                    visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
                visualMap += "   ";
                for (int j = 0; j < map1.length * 2 + 1; j++)
                    visualMap += (ConsoleColors.BLACK_BACKGROUND_BRIGHT + "   " + ConsoleColors.RESET);
                visualMap += "\n";
            }

        } else {

            visualMap += (dividerColor + "\n     1   2   3      4   5   6" + ConsoleColors.RESET + "\n    ─── ─── ───    ─── ─── ───\n");

            // Draw Top-Left and Top-Right Quadrants
            for (int i = 0; i < length; i++) {

                visualMap += " " + dividerColor + (i + 1) + ConsoleColors.RESET + " ";

                for (int j = 0; j < length; j++)
                    visualMap += "| " + map1[i][j].getColor() + map1[i][j].getValue() + ConsoleColors.RESET + " ";

                visualMap += "|  ";

                for (int j = 0; j < length; j++)
                    visualMap += "| " + map2[i][j].getColor() + map2[i][j].getValue() + ConsoleColors.RESET + " ";

                visualMap += "|\n    ─── ─── ───    ─── ─── ───\n";
            }

            visualMap += "    ─── ─── ───    ─── ─── ───\n";

            // Draw Bottom-Left and Bottom-Right Quadrants
            for (int i = 0; i < length; i++) {

                visualMap += " " + dividerColor + (i + 4) + ConsoleColors.RESET + " ";
                for (int j = 0; j < length; j++)
                    visualMap += "| " + map3[i][j].getColor() + map3[i][j].getValue() + ConsoleColors.RESET + " ";

                visualMap += "|  ";

                for (int j = 0; j < length; j++)
                    visualMap += "| " + map4[i][j].getColor() + map4[i][j].getValue() + ConsoleColors.RESET + " ";

                visualMap += "|\n    ─── ─── ───    ─── ─── ───\n";
            }
        }

        System.out.print(visualMap);
    }

    /**
     * This method transforms the foreColor to BackColor
     * @param color ForeColor
     * @return BackColor
     */
    private String transformColor(String color) {
        switch (color) {
            case ConsoleColors.GREEN: return ConsoleColors.GREEN_BACKGROUND;
            case ConsoleColors.RED: return ConsoleColors.RED_BACKGROUND;
            default: return ConsoleColors.WHITE_BACKGROUND_BRIGHT;
        }
    }

}