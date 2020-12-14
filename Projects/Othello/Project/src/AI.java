/**
 * AI object, well noting more is there to say, decides basically.
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Inheritance!!!
 * Inherited: protected void addModification(int k, int t, Cell[][] map, CellType type, List<Cell> list, List<Cell> temp)
 */
public class AI extends Game {

    /**
     * java.Util.random
     */
    private Random random = new Random();
    /**
     * Indicator
     */
    private boolean isBreak = false;
    /**
     * Network
     */
    NeuralNetwork network;

    /**
     * This function is the mind of the AI
     * <p>
     * Easy: Choose a complete random option that is available and return the coordinates
     * Medium:
     * Hard:
     *
     * @param map        Map matrix
     * @param options    A list of all options available
     * @param difficulty does what it says
     * @return coordinate form string
     */
    public String decide(Cell[][] map, List<Cell> options, String difficulty) {
        String rowStr = null, columnStr = null;

        switch (difficulty) {
            case "easy":    // Keep calm and play easy-peasy (;
                int randomIndex = random.nextInt(options.size());
                rowStr = String.valueOf(options.get(randomIndex).i + 1);
                columnStr = String.valueOf((char) (options.get(randomIndex).j + 65));
                break;
            case "medium":  // A bit more sophisticated, yet still, beatable

                int maxScore = 0;
                int bestMoveX = -1, bestMoveY = -1; // Initialized, just to be safe
                for (Cell option: options) {
                    Cell[][] clonedMap = new Cell[map.length][map.length];
                    for (int i = 0; i < map.length; i++)
                        for (int j = 0; j < map.length; j++)
                            clonedMap[i][j] = new Cell(map[i][j].getType(), map[i][j].i, map[i][j].j);

                    // Change the target cell
                    if (option.getType() == CellType.BLACK)
                        clonedMap = modify(option.i, option.j, clonedMap, CellType.WHITE);
                    else
                        clonedMap = modify(option.i, option.j, clonedMap, CellType.BLACK);

                    // Calculate overall score of this particular move
                    int tempScore = 0;
                    for (int i = 0; i < clonedMap.length; i++)
                        for (int j = 0; j < clonedMap.length; j++)
                            if (clonedMap[i][j].getType() == option.getType())
                                tempScore++;

                    if (tempScore >= maxScore) {
                        maxScore = tempScore;
                        bestMoveX = option.i;
                        bestMoveY = option.j;
                    }
                }

                rowStr = String.valueOf(bestMoveX + 1);
                columnStr =  String.valueOf((char)(bestMoveY + 65));

                break;
            case "hard":    // Aaaaah, This part is what I'm talking 'bout

                network = new NeuralNetwork(map);
                String desicion = network.decide(options);
                rowStr = String.valueOf(Integer.parseInt(desicion.split(" ")[0]) + 1);
                columnStr = String.valueOf((char)(Integer.parseInt(desicion.split(" ")[1]) + 65));

                break;
        }

        return rowStr + " " + columnStr;
    }

    /**
     * This method modifies the cells in the middle after player's move (See Ruls)
     *
     * @param i    Indexer
     * @param j    Indexer
     * @param map  Board map
     * @param type Cell type
     * @override Game.modify
     */
    protected Cell[][] modify(int i, int j, Cell[][] map, CellType type) {

        List<Cell> list = new ArrayList<>();
        List<Cell> temp = new ArrayList<>();

        // Right
        isBreak = false;
        temp.clear();
        for (int k = i, t = j + 1; t < map.length; t++)
            addModification(k, t, map, type, list, temp);

        // Left
        isBreak = false;
        temp.clear();
        for (int k = i, t = j - 1; t >= 0; t--)
            addModification(k, t, map, type, list, temp);

        // Down
        isBreak = false;
        temp.clear();
        for (int k = i + 1, t = j; k < map.length; k++)
            addModification(k, t, map, type, list, temp);

        // Up
        isBreak = false;
        temp.clear();
        for (int k = i - 1, t = j; k >= 0; k--)
            addModification(k, t, map, type, list, temp);

        // Up-Left
        isBreak = false;
        temp.clear();
        for (int k = i - 1, t = j - 1; k >= 0 && t >= 0; k--, t--)
            addModification(k, t, map, type, list, temp);

        // Up-Right
        isBreak = false;
        temp.clear();
        for (int k = i - 1, t = j + 1; k >= 0 && t < map.length; k--, t++)
            addModification(k, t, map, type, list, temp);

        // Down-Left
        isBreak = false;
        temp.clear();
        for (int k = i + 1, t = j - 1; k < map.length && t >= 0; k++, t--)
            addModification(k, t, map, type, list, temp);

        // Down-Right
        isBreak = false;
        temp.clear();
        for (int k = i + 1, t = j + 1; k < map.length && t < map.length; k++, t++)
            addModification(k, t, map, type, list, temp);

        for (Cell cell : list) {
            if (type == CellType.WHITE)
                cell.setType(CellType.BLACK);
            else
                cell.setType(CellType.WHITE);

            map[cell.i][cell.j] = cell;
        }

        return map;
    }
}