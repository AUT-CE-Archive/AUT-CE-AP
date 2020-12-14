import java.util.List;

/**
 * Network is simple: It contains input and output layers only
 * Network must be 'Dense', But I have no idea how to do that right now, so we just skip to the other parts
 * I know how to do 'Reinforcement Learning' but donno how to combine it with NN, so this part, we skip as well (Maybe not)
 *
 * @author Keivan Ipchi Hagh
 * @version 1.0.2
 * @since 2020-28-3
 */
public class NeuralNetwork extends AI {

    InputLayer[][] input_layer = new InputLayer[8][8];
    Gene[][] genes = new Gene[8][8];
    float[][] weights;

    /**
     * Constructor
     */
    public NeuralNetwork(Cell[][] map) {
        weights = new float[][]{
                {4, 4, 4, 4, 4, 4, 4, 4},
                {4, 3, 3, 3, 3, 3, 3, 4},
                {4, 3, 2, 2, 2, 2, 3, 4},
                {4, 3, 2, 1, 1, 2, 3, 4},
                {4, 3, 2, 1, 1, 2, 3, 4},
                {4, 3, 2, 2, 2, 2, 3, 4},
                {4, 3, 3, 3, 3, 3, 3, 4},
                {4, 4, 4, 4, 4, 4, 4, 4},
        };

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {

                // Clone
                Cell[][] clonedMap = new Cell[map.length][map.length];
                for (int t = 0; t < map.length; t++)
                    for (int k = 0; k < map.length; k++)
                        clonedMap[t][k] = new Cell(map[t][k].getType(), map[t][k].i, map[t][k].j);

                // Change the target cell
                clonedMap = modify(map[i][j].i, map[i][j].j, clonedMap, CellType.BLACK);

                // Calculate overall score of this particular move
                int tempScore = 0;
                for (int t = 0; t < map.length; t++)
                    for (int k = 0; k < map.length; k++)
                        if (clonedMap[t][k].getType() == CellType.WHITE)
                            tempScore++;

                genes[i][j] = new Gene(i, j, tempScore);
            }
        }
    }

    public String decide(List<Cell> options) {

        Gene bestGene = null;
        float bestScore = 0;
        for (int i = 0; i < genes.length; i++)
            for (int j = 0; j < genes.length; j++) {

                // Configure Bias
                float bias = (float) 0.001;
                for (Cell cell: options)
                    if (cell.i == genes[i][j].i && cell.j == genes[i][j].j) {
                        bias = 1;
                        break;
                    }

                input_layer[i][j] = new InputLayer(weights[i][j], bias);    // Initialize
                float temp =input_layer[i][j].perceptron(genes[i][j].getFitness());
                if (temp >= bestScore) {
                    bestGene = genes[i][j];
                    bestScore = temp;
                }
        }

        return bestGene.i + " " + bestGene.j;
    }
}

class Gene {

    private float fitness;
    int i, j;

    /**
     * Constructor
     * @param i i
     * @param j j
     * @param fitness Fitness
     */
    public Gene(int i, int j, float fitness) {
        this.i = i;
        this.j = j;
        this.fitness = fitness;
    }

    /**
     * Get fitness
     * @return Fitness
     */
    public float getFitness() {
        return fitness;
    }
}

class InputLayer {

    float weight, bias;

    /**
     * Constructor
     */
    public InputLayer(float weight, float bias) {
        this.weight = weight;
        this.bias = bias;
    }

    /**
     * Sigmoid Activation function
     * @param number input
     * @return output
     */
    private float sigmoid(float number) {
        return 1 / (1 + (float)Math.exp(-1 * number));
    }

    /**
     * Rectifier Activation function
     * @param number input
     * @return output
     */
    private float reLU(float number) {
        return number <= 0 ? 0 : number;
    }

    /**
     * Perceptron
     * @param fitness Fitness
     * @return score
     */
    public float perceptron(float fitness) {
        return fitness * weight * bias;
    }
}