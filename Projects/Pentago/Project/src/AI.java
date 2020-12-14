import java.util.Random;

public class AI {

    Random random = new Random();

    /**
     * Decides based on difficulty
     * @param map Full map
     * @param difficulty difficulty
     * @return Input
     */
    public String decide(Cell[][] map, String difficulty, Player player) {
        switch (difficulty) {
            case "easy":    // Complete chaos (;
                while (true) {
                    int x = random.nextInt(map.length);
                    int y = random.nextInt(map.length);
                    String rotation = (random.nextInt(2) == 0 ? "true" : "false");
                    String quadrant = "";
                    switch (random.nextInt(4)) {
                        case 0: quadrant = "TL"; break;
                        case 1: quadrant = "TR"; break;
                        case 2: quadrant = "BL"; break;
                        case 3: quadrant = "BR"; break;
                    }
                    if (map[x][y].getColor().equals(ConsoleColors.BLACK))
                        return (y + 1) + " " + (x + 1) + " " + quadrant + " " + rotation;
                }
            case "medium":
                int maxScore = -1;
                int selectedX = -1, selectedY = -1;
                for (int i = 0; i < map.length; i++)
                    for (int j = 0; j < map.length; j++) {
                        int tempScore = neighbor_scoring(map, i, j, player);
                        if (tempScore >= maxScore && map[i][j].getColor().equals(ConsoleColors.BLACK)) {
                            maxScore = tempScore;
                            selectedX = i;
                            selectedY = j;
                        }
                    }

                String rotation = (random.nextInt(2) == 0 ? "true" : "false");
                String quadrant = "";
                switch (random.nextInt(4)) {
                    case 0: quadrant = "TL"; break;
                    case 1: quadrant = "TR"; break;
                    case 2: quadrant = "BL"; break;
                    case 3: quadrant = "BR"; break;
                }

                return (selectedY + 1) + " " + (selectedX + 1) + " " + quadrant + " " + rotation;
            case "hard":
                maxScore = -1;
                selectedX = -1;
                selectedY = -1;
                for (int i = 0; i < map.length; i++)
                    for (int j = 0; j < map.length; j++) {
                        int tempScore = neighbor_scoring(map, i, j, player);
                        if (tempScore >= maxScore && map[i][j].getColor().equals(ConsoleColors.BLACK)) {
                            maxScore = tempScore;
                            selectedX = i;
                            selectedY = j;
                        }
                    }

                if (selectedX == 0 && selectedY == 0)
                    decide(map, "easy", player);

                rotation = (random.nextInt(2) == 0 ? "true" : "false");
                quadrant = "";
                switch (random.nextInt(4)) {
                    case 0: quadrant = "TL"; break;
                    case 1: quadrant = "TR"; break;
                    case 2: quadrant = "BL"; break;
                    case 3: quadrant = "BR"; break;
                }

                return (selectedY + 1) + " " + (selectedX + 1) + " " + quadrant + " " + rotation;
        }

        return "";
    }

    /**
     * Return the total score of the cell
     * @param map Map
     * @param x X-Axis
     * @param y Y-Axis
     * @param player player Obj
     * @return Score
     */
    public int neighbor_scoring(Cell[][] map, int x, int y, Player player) {
        int score = 0;
        try {
            if (map[x - 1][y].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x + 1][y].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x][y - 1].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x][y + 1].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x - 1][y - 1].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x - 1][y + 1].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x + 1][y - 1].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}
        try {
            if (map[x + 1][y + 1].getColor().equals(player.cell.getColor()))
                score++;
        } catch (Exception ignored) {}

        return score;
    }
}